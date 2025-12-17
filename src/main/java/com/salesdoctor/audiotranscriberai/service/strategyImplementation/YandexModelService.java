package com.salesdoctor.audiotranscriberai.service.strategyImplementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesdoctor.audiotranscriberai.enums.AiModelType;
import com.salesdoctor.audiotranscriberai.service.AudioProcessingService;
import com.salesdoctor.audiotranscriberai.service.strategyInterface.TranscriptionStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

@Service
public class YandexModelService implements TranscriptionStrategy {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final AudioProcessingService audioService;

    @Value("${spring.ai.yandex.api-key}")
    private String apiKey;

    @Value("${spring.ai.yandex.base-url}")
    private String baseUrl;

    public YandexModelService(RestClient.Builder restClientBuilder,
                              ObjectMapper objectMapper,
                              AudioProcessingService audioService) {
        this.restClient = restClientBuilder.build();
        this.objectMapper = objectMapper;
        this.audioService = audioService;
    }

    @Override
    public AiModelType getModelType() {
        return AiModelType.YANDEX;
    }

    @Override
    public String transcribe(MultipartFile audioFile) {
        List<File> processedFiles = null;
        try {
            processedFiles = audioService.prepareAudioForYandex(audioFile);
            StringBuilder fullText = new StringBuilder();

            for (File file : processedFiles) {
                byte[] bytes = Files.readAllBytes(file.toPath());

                String chunkResult = sendToYandex(bytes);
                if (!chunkResult.isEmpty()) {
                    fullText.append(chunkResult).append(" ");
                }
            }

            return fullText.toString().trim();

        } catch (Exception e) {
            return "Yandex Processing Error: " + e.getMessage();
        } finally {
            if (processedFiles != null) {
                for (File f : processedFiles) {
                    if (f.exists()) f.delete();
                }
            }
        }
    }

    private String sendToYandex(byte[] audioBytes) {
        try {
            String uri = UriComponentsBuilder.fromUriString(baseUrl)
                    .queryParam("lang", "uz-UZ")
                    .queryParam("topic", "general")
                    .queryParam("profanityFilter", "false")
                    .queryParam("format", "oggopus")
                    .toUriString();

            String response = restClient.post()
                    .uri(uri)
                    .header("Authorization", "Api-Key " + apiKey)
                    .body(audioBytes)
                    .retrieve()
                    .body(String.class);

            JsonNode root = objectMapper.readTree(response);
            if (root.has("result")) {
                return root.path("result").asText();
            }
            return "";
        } catch (Exception e) {
            System.err.println("Yandex Request Failed: " + e.getMessage());
            return "";
        }
    }
}
