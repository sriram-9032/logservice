package com.dtt.logs.configuration;

//import java.security.cert.X509Certificate;
//
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

//    public static void disableSslVerification() throws Exception {
//        TrustManager[] trustAll = new TrustManager[]{
//                new X509TrustManager() {
//                    public X509Certificate[] getAcceptedIssuers() { return null; }
//                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
//                        //
//                         }
//                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
//                        //
//                         }
//                }
//        };
//
//        SSLContext sc = SSLContext.getInstance("TLS");
//        sc.init(null, trustAll, new java.security.SecureRandom());
//        SSLContext.setDefault(sc);
//    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}