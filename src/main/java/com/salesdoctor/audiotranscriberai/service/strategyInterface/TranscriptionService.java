package com.salesdoctor.audiotranscriberai.service.strategyInterface;

import com.salesdoctor.audiotranscriberai.enums.AiModelType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TranscriptionService {

    private final Map<AiModelType, TranscriptionStrategy> strategyMap;

    public TranscriptionService(List<TranscriptionStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(TranscriptionStrategy::getModelType, Function.identity()));
    }

    public String transcribeAudio(String modelName, MultipartFile file) {
        AiModelType type;
        try {
            type = AiModelType.fromString(modelName);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported model. Available: GEMINI, ELEVENLABS, GROQ, ASSEMBLY");
        }

        TranscriptionStrategy strategy = strategyMap.get(type);
        if (strategy == null) {
            throw new UnsupportedOperationException("Strategy implementation not found for " + type);
        }

        return strategy.transcribe(file);
    }
}