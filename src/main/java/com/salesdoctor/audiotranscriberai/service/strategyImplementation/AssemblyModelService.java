package com.salesdoctor.audiotranscriberai.service.strategyImplementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesdoctor.audiotranscriberai.enums.AiModelType;
import com.salesdoctor.audiotranscriberai.service.strategyInterface.TranscriptionStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
public class AssemblyModelService implements TranscriptionStrategy {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${spring.ai.assemblyai.api-key}")
    private String apiKey;

    @Value("${spring.ai.assemblyai.base-url}")
    private String baseUrl;

    public AssemblyModelService(RestClient.Builder restClientBuilder, ObjectMapper objectMapper) {
        this.restClient = restClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    @Override
    public AiModelType getModelType() {
        return AiModelType.ASSEMBLY;
    }

    @Override
    public String transcribe(MultipartFile audioFile) {
        try {
            String uploadUrl = uploadFile(audioFile);

            String transcriptId = requestTranscription(uploadUrl);

            return waitForCompletion(transcriptId);

        } catch (Exception e) {
            return "AssemblyAI Error: " + e.getMessage();
        }
    }

    private String uploadFile(MultipartFile file) throws Exception {
        String response = restClient.post()
                .uri(baseUrl + "/upload")
                .header("Authorization", apiKey)
                .body(file.getBytes())
                .retrieve()
                .body(String.class);

        return objectMapper.readTree(response).path("upload_url").asText();
    }

    private String requestTranscription(String audioUrl) throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("audio_url", audioUrl);
        body.put("language_detection", true);
        body.put("punctuate", true);
        body.put("format_text", true);

        String response = restClient.post()
                .uri(baseUrl + "/transcript")
                .header("Authorization", apiKey)
                .header("Content-Type", "application/json")
                .body(body)
                .retrieve()
                .body(String.class);

        return objectMapper.readTree(response).path("id").asText();
    }

    private String waitForCompletion(String transcriptId) throws Exception {
        while (true) {
            String response = restClient.get()
                    .uri(baseUrl + "/transcript/" + transcriptId)
                    .header("Authorization", apiKey)
                    .retrieve()
                    .body(String.class);

            JsonNode root = objectMapper.readTree(response);
            String status = root.path("status").asText();

            if ("completed".equals(status)) {
                return root.path("text").asText();
            } else if ("error".equals(status)) {
                throw new RuntimeException("Transcription failed: " + root.path("error").asText());
            }
            Thread.sleep(1000);
        }
    }
}