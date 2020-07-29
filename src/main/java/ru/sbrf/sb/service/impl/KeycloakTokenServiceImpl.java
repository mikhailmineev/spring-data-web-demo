package ru.sbrf.sb.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.TokenVerifier;
import org.keycloak.common.VerificationException;
import org.keycloak.jose.jws.JWSHeader;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.JsonWebToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import ru.sbrf.sb.service.KeycloakTokenService;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakTokenServiceImpl implements KeycloakTokenService {
    private static final String AUTHENTICATION_SCHEME = "Bearer";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final URL certsUrl;
    private final String key;

    private final TokenVerifier.Predicate<JsonWebToken>[] checks;

    public KeycloakTokenServiceImpl(
            @Value("${keycloak.host}") URI keycloakHost,
            @Value("${keycloak.realm}") String keycloakRealm,
            @Value("${keycloak.key}") String key) throws MalformedURLException {
        this.certsUrl = getRealmCertsUrl(keycloakHost, keycloakRealm);
        this.key = key;
        this.checks = new TokenVerifier.Predicate[]{TokenVerifier.SUBJECT_EXISTS_CHECK, TokenVerifier.IS_ACTIVE};
    }

    @Override
    public AccessToken extractAccessTokenFrom(String token) throws VerificationException {
        if (token == null) {
            return null;
        }

        TokenVerifier<AccessToken> verifier = TokenVerifier.create(token, AccessToken.class);
        //PublicKey publicKey = getRealmPublicKey(verifier.getHeader());
        PublicKey publicKey = getKey(key);
        return verifier.publicKey(publicKey)
                .withChecks(checks)
                .verify()
                .getToken();
    }

    private static PublicKey getKey(String key){
        try{
            byte[] byteKey = Base64.getDecoder().decode(key.getBytes());
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");

            return kf.generatePublic(X509publicKey);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
    @Override
    public AccessToken authenticateFromAuthHeader(String token) throws VerificationException {
        token = token.substring(AUTHENTICATION_SCHEME.length()).trim();
        return extractAccessTokenFrom(token);
    }

    private URL getRealmCertsUrl(URI serverUrl, String realmId) throws MalformedURLException {
        return UriComponentsBuilder.fromUri(serverUrl)
                .pathSegment("realms", realmId, "protocol", "openid-connect", "certs").build().toUri().toURL();
    }

    private PublicKey getRealmPublicKey(JWSHeader jwsHeader) {
        return retrievePublicKeyFromCertsEndpoint(jwsHeader);
    }

    private PublicKey retrievePublicKeyFromCertsEndpoint(JWSHeader jwsHeader) {
        try {
            Map<String, Object> certInfos = objectMapper.readValue(certsUrl.openStream(), Map.class);

            List<Map<String, Object>> keys = (List<Map<String, Object>>) certInfos.get("keys");

            Map<String, Object> keyInfo = null;
            for (Map<String, Object> key : keys) {
                String kid = (String) key.get("kid");

                if (jwsHeader.getKeyId().equals(kid)) {
                    keyInfo = key;
                    break;
                }
            }

            if (keyInfo == null) {
                return null;
            }

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            String modulusBase64 = (String) keyInfo.get("n");
            String exponentBase64 = (String) keyInfo.get("e");

            // see org.keycloak.jose.jwk.JWKBuilder#rs256
            Decoder urlDecoder = Base64.getUrlDecoder();
            BigInteger modulus = new BigInteger(1, urlDecoder.decode(modulusBase64));
            BigInteger publicExponent = new BigInteger(1, urlDecoder.decode(exponentBase64));

            return keyFactory.generatePublic(new RSAPublicKeySpec(modulus, publicExponent));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

