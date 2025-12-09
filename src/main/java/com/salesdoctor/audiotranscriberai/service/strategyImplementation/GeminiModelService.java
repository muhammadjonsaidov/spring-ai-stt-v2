package com.salesdoctor.audiotranscriberai.service.strategyImplementation;

import com.salesdoctor.audiotranscriberai.enums.AiModelType;
import com.salesdoctor.audiotranscriberai.service.strategyInterface.TranscriptionStrategy;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GeminiModelService implements TranscriptionStrategy {
    private final ChatModel chatModel;

    public GeminiModelService(@Qualifier("googleGenAiChatModel") ChatModel chatModel) {
        this.chatModel = chatModel;
    }


    @Override
    public String transcribe(MultipartFile audioFile) {

        try {
            ByteArrayResource resource = new ByteArrayResource(audioFile.getBytes());

            String fileName = audioFile.getOriginalFilename();
            String fixedMimeType = getSupportedMimeType(fileName);

            MimeType mimeType = MimeTypeUtils.parseMimeType(fixedMimeType);
            Media media = new Media(mimeType, resource);
            UserMessage userMessage = UserMessage.builder()
                    .text("Iltimos, ushbu audio faylni to'liq matnga aylantirib ber (transkripsiya qil). Faqat matnni yoz, boshqa gap qo'shma.")
                    .media(media)
                    .build();

            return chatModel.call(new Prompt(userMessage)).getResult().getOutput().getText();
        } catch (Exception e) {
            throw new RuntimeException("Error processing audio file for Gemini", e);
        }
    }

    @Override
    public AiModelType getModelType() {
        return AiModelType.GEMINI;
    }

    private String getSupportedMimeType(String fileName) {
        if (fileName == null) return "audio/mpeg";

        String lowerCaseName = fileName.toLowerCase();

        if (lowerCaseName.endsWith(".ogg") || lowerCaseName.endsWith(".oga")) {
            return "audio/ogg";
        }
        if (lowerCaseName.endsWith(".mp3")) {
            return "audio/mpeg";
        }
        if (lowerCaseName.endsWith(".wav")) {
            return "audio/wav";
        }
        if (lowerCaseName.endsWith(".m4a") || lowerCaseName.endsWith(".mp4")) {
            return "audio/mp4";
        }
        if (lowerCaseName.endsWith(".flac")) {
            return "audio/flac";
        }
        if (lowerCaseName.endsWith(".aac")) {
            return "audio/aac";
        }
        if (lowerCaseName.endsWith(".webm")) {
            return "audio/webm";
        }
        if (lowerCaseName.endsWith(".amr")) {
            return "audio/amr";
        }
        return "audio/mpeg";
    }

}
