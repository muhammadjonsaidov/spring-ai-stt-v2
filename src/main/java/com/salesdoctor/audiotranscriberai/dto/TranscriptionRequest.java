package com.salesdoctor.audiotranscriberai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

public class TranscriptionRequest {

    @Schema(description = "Audio file to be transcribed", type = "string", format = "binary", requiredMode = Schema.RequiredMode.REQUIRED)
    private MultipartFile file;

    @Schema(description = "AI Model to use", example = "gemini", allowableValues = {"gemini", "elevenlabs", "groq", "assembly", "yandex"})
    private String model;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}