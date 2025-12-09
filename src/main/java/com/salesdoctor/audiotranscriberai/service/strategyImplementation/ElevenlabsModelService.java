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

@Service
public class ElevenlabsModelService implements TranscriptionStrategy {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${spring.ai.elevenlabs.base-url}")
    private String baseUrl;
    @Value("${spring.ai.elevenlabs.api-key}")
    private String apiKey;
    @Value("${spring.ai.elevenlabs.tts.options.model}")
    private String model;

    public ElevenlabsModelService(RestClient.Builder restClientBuilder, ObjectMapper objectMapper) {
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
            body.add("model_id", model);
            body.add("language_code", "uz");

            String response = restClient.post()
                    .uri(baseUrl + "/speech-to-text") // Base URL + endpoint
                    .header("xi-api-key", apiKey)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(body)
                    .retrieve()
                    .body(String.class);

            return objectMapper.readTree(response).path("text").asText();
        } catch (Exception e) {
            throw new RuntimeException("ElevenLabs transcription failed: " + e.getMessage(), e);
        }
    }

    @Override
    public AiModelType getModelType() {
        return AiModelType.ELEVENLABS;
    }
}
