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
                        new Server().url("https://3541f2c830e3.ngrok-free.app")
                ))
                .info(new Info()
                        .title("STT API")
                        .version("1.0")
                        .description("API for transcribing audio to text (STT)"));
    }
}
