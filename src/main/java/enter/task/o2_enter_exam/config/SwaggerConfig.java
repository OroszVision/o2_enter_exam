package enter.task.o2_enter_exam.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityRequirement;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("users") // This is just a label for your API group
                .pathsToMatch("/**")  // This will include all paths (you can refine this if needed)
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")  // Indicates that it's a JWT token
                                .in(In.HEADER)
                                .name("Authorization")))  // The Authorization header
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearer-jwt")); // Apply security requirement globally
    }
}
