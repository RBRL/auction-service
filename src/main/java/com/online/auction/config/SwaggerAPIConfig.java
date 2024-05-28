package com.online.auction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
	public class SwaggerAPIConfig {

	    @Bean
	    public Docket api() {
	        return new Docket(DocumentationType.SWAGGER_2)
	                .select()
	                .apis(RequestHandlerSelectors.basePackage("com.online.auction"))
	                .paths(PathSelectors.any())
	                .build().apiInfo(apiInfoMetaData());
	    }

	    private ApiInfo apiInfoMetaData() {

	        return new ApiInfoBuilder().title("Auction Service API Documentation")
	                .description("Auction service API's")
	                .license("Apache 2.0")
	                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
	                .version("1.0.0")
	                .build();
	    }
	}
