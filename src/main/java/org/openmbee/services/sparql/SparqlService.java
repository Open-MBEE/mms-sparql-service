package org.openmbee.services.sparql;

import java.util.Arrays;
import java.util.Optional;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = "org.openmbee")
@EnableSwagger2
public class SparqlService {

    public static void main(String[] args) {
        SpringApplication.run(SparqlService.class, args);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(Optional.class)
                .apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any()).build()
                .securitySchemes(Arrays.asList(
                        new BasicAuth("basicAuth"),
                        new ApiKey("bearerToken", "Authorization", "header")))
                .securityContexts(Arrays.asList(
                        SecurityContext.builder().securityReferences(Arrays.asList(
                                new SecurityReference("basicAuth", new AuthorizationScope[0]),
                                new SecurityReference("bearerToken", new AuthorizationScope[0])
                        )).forPaths(PathSelectors.any()).build()
                ));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("MMS Sparql API")
                .description("Documentation for MMS Sparql API").termsOfServiceUrl("")
                .contact(new Contact("OpenMBEE", "http://www.openmbee.org",
                        ""))
                .license("Apache License Version 2.0").licenseUrl("").version("2.0").build();
    }
}
