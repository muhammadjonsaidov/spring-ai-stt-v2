package com.salesdoctor.audiotranscriberai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local server"),
                        new Server().url("https://4752e85c2495.ngrok-free.app/").description("Ngrok server")
                ))
                .info(new Info()
                        .title("STT API")
                        .version("1.0")
                        .description("API for transcribing audio to text (STT)"));
    }
}
