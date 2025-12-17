package com.salesdoctor.audiotranscriberai.enums;

public enum AiModelType {
    GEMINI,
    ELEVENLABS,
    GROQ,
    ASSEMBLY,
    YANDEX;


    public static AiModelType fromString(String modelName) {
        for (AiModelType b : AiModelType.values()) {
            if (b.name().equalsIgnoreCase(modelName)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unknown model: " + modelName);
    }
}
