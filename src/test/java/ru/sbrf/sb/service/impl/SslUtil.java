package ru.sbrf.sb.service.impl;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class SslUtil {

    public SSLContext getSslContext() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");

            char[] keyStorePasswordArray = "password".toCharArray();
            KeyStore ks = getKeyStoreFromFile("./src/main/resources/keystore.jks", "password");

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(ks, keyStorePasswordArray);

            KeyStore ts = getKeyStoreFromFile("./src/main/resources/truststore.jks", "password");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(ts);

            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            return sslContext;
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException | UnrecoverableKeyException e) {
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
}
