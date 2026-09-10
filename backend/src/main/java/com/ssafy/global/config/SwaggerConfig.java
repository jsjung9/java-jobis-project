package com.ssafy.global.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi
                .builder()
                .group("jobis-api")
                .pathsToMatch("/**")
                .addOpenApiCustomizer(
                        openApi ->
                                openApi
                                        .setInfo(
                                                new Info()
                                                        .title("Jobis API")
                                                        .description("강소기업 탐색과 추천 서비스 API")
                                                        .version("1.0.0")
                                        )
                )
                .build();
    }
}
