package ru.sbrf.sb.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient43Engine;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class HttpClient {

    private HttpClient() {
        // FODBIDDEN
    }

    private static final int TOTAL_CONNECTIONS = 100;
    private static final int CONNECTIONS_PER_ROUTE = 10;
    private static final String HTTPS = "https";
    private static final String HTTP = "http";

    public static ResteasyClient getHttpsClient() {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder
                .<ConnectionSocketFactory>create()
                .register(HTTP, PlainConnectionSocketFactory.getSocketFactory());

        SSLContext sslContext = new SslUtil().getSslContext();
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
                sslContext, new NoopHostnameVerifier());
        registryBuilder.register(HTTPS, sslSocketFactory);

        final Registry<ConnectionSocketFactory> socketFactoryRegistry = registryBuilder.build();

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        cm.setMaxTotal(TOTAL_CONNECTIONS);
        cm.setDefaultMaxPerRoute(CONNECTIONS_PER_ROUTE);
        CloseableHttpClient httpClient = HttpClients
                .custom()
                .useSystemProperties()
                .setConnectionManager(cm)
                .build();

        return new ResteasyClientBuilder()
                .httpEngine(new ApacheHttpClient43Engine(httpClient))
                .hostnameVerification(ResteasyClientBuilder.HostnameVerificationPolicy.ANY)
                .build();
    }
}
