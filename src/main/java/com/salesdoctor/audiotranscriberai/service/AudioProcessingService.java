package com.salesdoctor.audiotranscriberai.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class AudioProcessingService {
    public List<File> prepareAudioForYandex(MultipartFile originalFile) throws Exception {
        List<File> readyChunks = new ArrayList<>();
        File source = File.createTempFile("source_", "_" + originalFile.getOriginalFilename());
        originalFile.transferTo(source);

        try {
            MultimediaObject mediaInfo = new MultimediaObject(source);
            long durationMillis = mediaInfo.getInfo().getDuration();
            double durationSeconds = durationMillis / 1000.0;

            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("libopus");
            audio.setBitRate(24000);
            audio.setChannels(1);
            audio.setSamplingRate(16000);

            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setOutputFormat("ogg");
            attrs.setAudioAttributes(audio);

            if (durationSeconds <= 29.0) {
                File target = File.createTempFile("processed_full_", ".ogg");
                Encoder encoder = new Encoder();
                encoder.encode(mediaInfo, target, attrs);
                readyChunks.add(target);
            } else {
                float offset = 0;
                float chunkDuration = 25.0f;
                int part = 1;

                while (offset < durationSeconds) {
                    File target = File.createTempFile("chunk_" + part + "_", ".ogg");
                    attrs.setOffset(offset);
                    attrs.setDuration(chunkDuration);

                    Encoder encoder = new Encoder();
                    encoder.encode(mediaInfo, target, attrs);

                    readyChunks.add(target);
                    offset += chunkDuration;
                    part++;
                }
            }
            return readyChunks;
        } finally {
            if (source.exists()) source.delete();
        }
    }
}
