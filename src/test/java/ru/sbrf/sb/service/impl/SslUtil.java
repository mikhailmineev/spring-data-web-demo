package ru.sbrf.sb.service.impl;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.*;
import java.security.cert.CertificateException;

public class SslUtil {
    // curl -k --cert ~/localhost.crt --key ~/localhost.key -vvvv https://localhost:32773

    /*
    docker run -d --rm \
    -e MOCKSERVER_TLS_MUTUAL_AUTHENTICATION_REQUIRED=true \
    -e MOCKSERVER_TLS_MUTUAL_AUTHENTICATION_CERTIFICATE_CHAIN=/opt/localhost.crt \
    -v $(pwd)/localhost.crt:/opt/localhost.crt \
    -P mockserver/mockserver
    */

    public SSLContext getSslContext(String keyStore, String keyStorePassword) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");

            char[] keyStorePasswordArray = keyStorePassword.toCharArray();
            KeyStore ks = getKeyStoreFromFile(keyStore, keyStorePassword);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(ks, keyStorePasswordArray);

            KeyStore ts = getKeyStoreFromFile(keyStore, keyStorePassword);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(ts);

            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            return sslContext;
        } catch (IOException | CertificateException | NoSuchAlgorithmException |
                UnrecoverableKeyException | KeyStoreException | KeyManagementException e) {
            throw new RuntimeException(e);
        }

    }

    public KeyStore getKeyStoreFromFile(String keyStorePath, String keyStorePassword)
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {

        KeyStore keyStore = KeyStore.getInstance("JKS");
        char[] trustStorePasswordArray = keyStorePassword.toCharArray();
        keyStore.load(new FileInputStream(keyStorePath), trustStorePasswordArray);
        return keyStore;
    }

    public SSLContext getSslContextIfNeeded(String serviceUrl, String keyStore, String keyStorePassword) {
        try {
            if ("https".equals(new URI(serviceUrl).getScheme())) {
                return getSslContext(keyStore, keyStorePassword);
            }
            return null;
        } catch (URISyntaxException e) {
            throw new RuntimeException("Ошибка инициализации SSL контекста", e);
        }
    }
}
