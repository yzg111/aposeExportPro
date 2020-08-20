package com.rf.cms.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


/**
 * 整个接口服务启动的类
 */
@SpringBootApplication
@ImportResource({"classpath:spring/*.xml"})
@EnableAutoConfiguration(
    exclude = {MultipartAutoConfiguration.class}
)
public class GPAMServer extends SpringBootServletInitializer {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean(
            name = {"multipartResolver"}
    )
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        resolver.setResolveLazily(true);
        resolver.setMaxInMemorySize('ꀀ');
        resolver.setMaxUploadSize(524288000L);
        return resolver;
    }

    
    public static void main(String[] args) {

        ApplicationContext appCtx = SpringApplication.run(GPAMServer.class, args);

    }

}
