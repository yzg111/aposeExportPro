package com.rf.cms.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Doc.manager.swagger swqgger接口配置类.
 */
@Configuration
@EnableSwagger2
public class swaggerConfig {
    @Bean
    public Docket createApi() {
        Docket swaggerSpringMvcPlugin =new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("所有的接口")
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.rf.cms.v3"))
                .paths(PathSelectors.any())
                .build();
        return swaggerSpringMvcPlugin;
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("RESTful APIs")
                .description("RESTful API")
                .termsOfServiceUrl("无")
                .contact("所有接口")
                .version("1.0")
                .build();
    }

//    //最上面的下拉框通过groupname来控制显示
//    @Bean
//    public Docket docApi() {
//        Docket swaggerSpringMvcPlugin =new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .groupName("接口")
//                .enable(false)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.rf.cms.v3"))
//                .paths(PathSelectors.any())
//                .build();
//        return swaggerSpringMvcPlugin;
//    }
//
//    //最上面的下拉框通过groupname来控制显示
//    @Bean
//    public Docket V7docApi() {
//        Docket swaggerSpringMvcPlugin =new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(v7apiInfo())
//                .groupName("第7版新加接口")
//                .enable(true)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.rf.cms.v3.v7"))
//                .paths(PathSelectors.any())
//                .build();
//        return swaggerSpringMvcPlugin;
//    }
//
//    @Bean
//    public Docket V8docApi() {
//        Docket swaggerSpringMvcPlugin =new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(v8apiInfo())
//                .groupName("第8版接口(统一代办的接口)")
//                .enable(true)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.rf.cms.v3.v8"))
//                .paths(PathSelectors.any())
//                .build();
//        return swaggerSpringMvcPlugin;
//    }
//
//    @Bean
//    public Docket V9docApi() {
//        Docket swaggerSpringMvcPlugin =new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(v9apiInfo())
//                .groupName("第9版接口(督查专区的接口)")
//                .enable(true)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.rf.cms.v3.v9"))
//                .paths(PathSelectors.any())
//                .build();
//        return swaggerSpringMvcPlugin;
//    }
//
//    private ApiInfo v9apiInfo() {
//        return new ApiInfoBuilder()
//                .title("RESTful APIs")
//                .description("RESTful API")
//                .termsOfServiceUrl("无")
//                .contact("上海元方")
//                .version("9.0")
//                .build();
//    }
//
//    private ApiInfo v8apiInfo() {
//        return new ApiInfoBuilder()
//                .title("RESTful APIs")
//                .description("RESTful API")
//                .termsOfServiceUrl("无")
//                .contact("上海元方")
//                .version("8.0")
//                .build();
//    }
//
//    private ApiInfo v7apiInfo() {
//        return new ApiInfoBuilder()
//                .title("RESTful APIs")
//                .description("RESTful API")
//                .termsOfServiceUrl("无")
//                .contact("上海元方")
//                .version("7.0")
//                .build();
//    }
}
