package com.salesdoctor.audiotranscriberai.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class TranscriptionResponse {

    @Schema(description = "Transcribed text result", example = "Hello, this is a transcribed text from the audio file.")
    private String transcription;

    public TranscriptionResponse(String transcription) {
        this.transcription = transcription;
    }

    // Getter and Setter
    public String getTranscription() { return transcription; }
    public void setTranscription(String transcription) { this.transcription = transcription; }
}