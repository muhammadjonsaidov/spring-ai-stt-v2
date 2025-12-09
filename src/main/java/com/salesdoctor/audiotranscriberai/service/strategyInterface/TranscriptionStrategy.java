package com.salesdoctor.audiotranscriberai.service.strategyInterface;

import com.salesdoctor.audiotranscriberai.enums.AiModelType;
import org.springframework.web.multipart.MultipartFile;

public interface TranscriptionStrategy {

    String transcribe(MultipartFile audioFile);

    AiModelType getModelType();
}
