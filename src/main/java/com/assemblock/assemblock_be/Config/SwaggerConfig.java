package com.assemblock.assemblock_be.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SERVICE_NAME = "Make Project";
    private static final String API_VERSION = "V1";
    private static final String API_DESCRIPTION = "MakeProject API TEST";

    @Bean
    public OpenAPI openAPI() {
        // JWT 설정 (스웨거 화면 오른쪽에 'Authorize' 자물쇠 버튼 생성)
        String jwtSchemeName = "JWT";

        // API 요청 시 헤더에 토큰을 같이 보내도록 설정
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        // SecuritySchemes 등록
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP) // HTTP 방식
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        // API 정보 설정 (타이틀, 설명 등)
        return new OpenAPI()
                .info(new Info()
                        .title(SERVICE_NAME)
                        .description(API_DESCRIPTION)
                        .version(API_VERSION))
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}