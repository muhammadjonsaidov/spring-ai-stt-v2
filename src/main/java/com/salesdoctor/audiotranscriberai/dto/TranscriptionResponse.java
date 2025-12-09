package com.salesdoctor.audiotranscriberai.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class TranscriptionResponse {

    @Schema(description = "Transcribed text result", example = "Hello, this is a transcribed text from the audio file.")
    private String text;

    public TranscriptionResponse(String text) {
        this.text = text;
    }

    // Getter and Setter
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}