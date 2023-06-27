package com.jxp.es.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ConfigurationProperties(prefix = "elasticsearch") //配置的前缀
@Configuration
@ConditionalOnProperty(prefix = "integration-es", name = "enable", havingValue = "true")
public class ClientConfig {

    @Setter
    private String hosts;

    @Setter
    private String username;

    @Setter
    private String passwd;

    @Setter
    private String apikey;

    /**
     * 解析配置的字符串，转为HttpHost对象数组
     */
    private HttpHost[] toHttpHost() {
        if (!StringUtils.hasLength(hosts)) {
            throw new RuntimeException("invalid elasticsearch configuration");
        }

        String[] hostArray = hosts.split(",");
        HttpHost[] httpHosts = new HttpHost[hostArray.length];
        HttpHost httpHost;
        for (int i = 0; i < hostArray.length; i++) {
            String[] strings = hostArray[i].split(":");
            httpHost = new HttpHost(strings[0], Integer.parseInt(strings[1]), "http");
            httpHosts[i] = httpHost;
        }

        return httpHosts;
    }

    @Bean
    @ConditionalOnMissingBean
    public ElasticsearchClient elasticsearchClient() {
        HttpHost[] httpHosts = toHttpHost();
        BasicCredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();
        basicCredentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, passwd));

        RestClient restClient = RestClient.builder(httpHosts)
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .setDefaultCredentialsProvider(basicCredentialsProvider))
                .build();
        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

    @Bean
    @ConditionalOnMissingBean
    public ElasticsearchAsyncClient elasticsearchAsyncClient() {
        HttpHost[] httpHosts = toHttpHost();
        BasicCredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();
        basicCredentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, passwd));
        RestClient restClient = RestClient.builder(httpHosts)
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder
                        .setDefaultCredentialsProvider(basicCredentialsProvider))
                .build();
        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchAsyncClient(transport);
    }

    //    @Bean
    //    public ElasticsearchClient clientByPasswd() throws Exception {
    //        ElasticsearchTransport transport = getElasticsearchTransport(username, passwd, toHttpHost());
    //        return new ElasticsearchClient(transport);
    //    }

    //    private static SSLContext buildSSLContext() {
    //        ClassPathResource resource = new ClassPathResource("es01.crt");
    //        SSLContext sslContext = null;
    //        try {
    //            CertificateFactory factory = CertificateFactory.getInstance("X.509");
    //            Certificate trustedCa;
    //            try (InputStream is = resource.getInputStream()) {
    //                trustedCa = factory.generateCertificate(is);
    //            }
    //            KeyStore trustStore = KeyStore.getInstance("pkcs12");
    //            trustStore.load(null, null);
    //            trustStore.setCertificateEntry("ca", trustedCa);
    //            SSLContextBuilder sslContextBuilder = SSLContexts.custom()
    //                    .loadTrustMaterial(trustStore, null);
    //            sslContext = sslContextBuilder.build();
    //        } catch (CertificateException | IOException | KeyStoreException | NoSuchAlgorithmException |
    //                KeyManagementException e) {
    //            log.error("ES连接认证失败", e);
    //        }
    //
    //        return sslContext;
    //    }

    //    private static ElasticsearchTransport getElasticsearchTransport(String username, String passwd, HttpHost...
    //    hosts) {
    //        // 账号密码的配置
    //        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    //        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, passwd));
    //
    //        // 自签证书的设置，并且还包含了账号密码
    //        HttpClientConfigCallback callback = httpAsyncClientBuilder -> httpAsyncClientBuilder
    //                .setSSLContext(buildSSLContext())
    //                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
    //                .setDefaultCredentialsProvider(credentialsProvider);
    //
    //        // 用builder创建RestClient对象
    //        RestClient client = RestClient
    //                .builder(hosts)
    //                .setHttpClientConfigCallback(callback)
    //                .build();
    //
    //        return new RestClientTransport(client, new JacksonJsonpMapper());
    //    }

    //    private static ElasticsearchTransport getElasticsearchTransport(String apiKey, HttpHost... hosts) {
    //        // 将ApiKey放入header中
    //        Header[] headers = new Header[] {new BasicHeader("Authorization", "ApiKey " + apiKey)};
    //
    //        // es自签证书的设置
    //        HttpClientConfigCallback callback = httpAsyncClientBuilder -> httpAsyncClientBuilder
    //                .setSSLContext(buildSSLContext())
    //                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
    //
    //        // 用builder创建RestClient对象
    //        RestClient client = RestClient
    //                .builder(hosts)
    //                .setHttpClientConfigCallback(callback)
    //                .setDefaultHeaders(headers)
    //                .build();
    //
    //        return new RestClientTransport(client, new JacksonJsonpMapper());
    //    }

    //    @Bean
    //    public ElasticsearchClient clientByApiKey() throws Exception {
    //        ElasticsearchTransport transport = getElasticsearchTransport(apikey, toHttpHost());
    //        return new ElasticsearchClient(transport);
    //    }
}
