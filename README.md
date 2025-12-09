# 🎙️ Spring AI STT Service V2

A robust Spring Boot application implementing the **Strategy Pattern** to perform **Speech-to-Text (STT)** conversion using **Groq (Whisper)**, **Google Gemini**, and **ElevenLabs**.

> **Repo:** [spring-ai-stt-v2](https://github.com/muhammadjonsaidov/spring-ai-stt-v2)

## 🚀 Key Features
- **Groq (whisper-large-v3-turbo):** Specifically optimized for **Uzbek, Russian, and English** mixed audio contexts with strict temperature settings.
- **Google Gemini:** Leverages multimodal capabilities for audio transcription.
- **ElevenLabs:** Integration with the Scribe model.
- **Strategy Pattern:** Easily switch or add new AI providers without changing core logic.
- **Swagger UI:** Integrated OpenAPI documentation.


## 🏗️ Tech Stack
- Java 21, Spring Boot 3.5.8 
- Spring AI, RestClient 
- Gradle