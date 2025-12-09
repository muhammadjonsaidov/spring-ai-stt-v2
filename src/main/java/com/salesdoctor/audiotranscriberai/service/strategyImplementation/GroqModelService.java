package com.salesdoctor.audiotranscriberai.service.strategyImplementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesdoctor.audiotranscriberai.enums.AiModelType;
import com.salesdoctor.audiotranscriberai.service.strategyInterface.TranscriptionStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class GroqModelService implements TranscriptionStrategy {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${spring.ai.groq.api-key}")
    private String apiKey;

    @Value("${spring.ai.groq.base-url}")
    private String baseUrl;

    @Value("${spring.ai.groq.options.model}")
    private String modelName;

    public GroqModelService(RestClient.Builder restClientBuilder, ObjectMapper objectMapper) {
        this.restClient = restClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    @Override
    public String transcribe(MultipartFile audioFile) {
        try {

            ByteArrayResource resource = new ByteArrayResource(audioFile.getBytes()) {
                @Override
                public String getFilename() {
                    return "audio.m4a";
                }
            };

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", resource);
            body.add("model", modelName);

            String contextPrompt = """
                    Ushbu audio suhbat O'zbek, Rus va Ingliz tillarida.
                    Iltimos, so'zlarni o'z tilida to'g'ri va aniq transkripsiya qiling.
                    O'zbekcha so'zlar(Latin). Русский текст. English words.
                    """;
            body.add("prompt", contextPrompt);
            body.add("temperature", 0);

            String response = restClient.post()
                    .uri(baseUrl + "/audio/transcriptions")
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(body)
                    .retrieve()
                    .body(String.class);

            return objectMapper.readTree(response).path("text").asText();
        } catch (IOException e) {
            return String.format("Error during transcription: %s", e.getMessage());
        }
    }

    @Override
    public AiModelType getModelType() {
        return AiModelType.GROQ;
    }
}
