package ru.sbrf.sb.service.impl;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;
import javax.ws.rs.core.UriBuilder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class SslTest {

    @Test
    public void test() {
        String fpaTokenUrl = "https://localhost:32771";

        var client = HttpClient.getHttpsClient();
        ResteasyWebTarget target = client.target(UriBuilder.fromPath(fpaTokenUrl));
        TokenClient tokenClient = target
                .proxyBuilder(TokenClient.class)
                .classloader(TokenClient.class.getClassLoader())
                .build();
        tokenClient.getToken();
    }
}
