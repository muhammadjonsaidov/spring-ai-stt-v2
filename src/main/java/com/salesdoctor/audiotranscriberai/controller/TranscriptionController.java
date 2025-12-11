package com.salesdoctor.audiotranscriberai.controller;

import com.salesdoctor.audiotranscriberai.dto.ModelsResponse;
import com.salesdoctor.audiotranscriberai.dto.TranscriptionRequest;
import com.salesdoctor.audiotranscriberai.dto.TranscriptionResponse;
import com.salesdoctor.audiotranscriberai.enums.AiModelType;
import com.salesdoctor.audiotranscriberai.service.strategyInterface.TranscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(originPatterns = "*")
public class TranscriptionController {

    private final TranscriptionService transcriptionService;

    public TranscriptionController(TranscriptionService transcriptionService) {
        this.transcriptionService = transcriptionService;
    }

    @Operation(summary = "Audio to Text", description = "Upload an audio file and specify the model (gemini, elevenlabs, groq, assembly).")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully transcribed",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TranscriptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request (Empty file or Invalid model)",
                    content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(type = "string", example = "File is empty OR Invalid model: ...")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(type = "string", example = "Processing error: Timeout Exception")
                    )
            )
    })
    @PostMapping(value = "/voice/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> transcribe(@ModelAttribute TranscriptionRequest requestDto) {

        if (requestDto.getFile() == null || requestDto.getFile().isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try {
            String result = transcriptionService.transcribeAudio(requestDto.getModel(), requestDto.getFile());
            TranscriptionResponse response = new TranscriptionResponse(result);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid model: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Processing error: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Get AI Models",
            description = "Returns all available AI model types for frontend selection."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully returned model list",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ModelsResponse.class)
                    )
            )
    })
    @GetMapping("/ai/models")
    public ResponseEntity<ModelsResponse> getModels() {
        List<String> modelNames = List.of(AiModelType.values()).stream()
                .map(Enum::name)
                .toList();
        ModelsResponse response = new ModelsResponse(modelNames);
        return ResponseEntity.ok(response);
    }
}