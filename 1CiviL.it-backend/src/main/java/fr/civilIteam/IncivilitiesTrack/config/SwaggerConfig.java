package fr.civilIteam.IncivilitiesTrack.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(@Value("${springdoc.version}") String appVersion) {

        return new OpenAPI()
                .info(new Info()
                        .title("1CiviL.it API")
                        .version(appVersion)
                        .description("This app allow some users to process reports of incivilities")
                        .license(new License().name("1CiviL.it").url("https://www.afpa.fr/centre/centre-de-roubaix"))
                        .contact(new Contact().name("1CiviL.it.fr").email("noreply.1civilit@gmail.com"))
                )
                .addSecurityItem(new SecurityRequirement().addList("Auth Token"))
                .components(new Components().addSecuritySchemes("Auth Token", new SecurityScheme()
                        .name("Auth Token").type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));

    }
}
