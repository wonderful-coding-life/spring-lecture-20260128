package com.example.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@SpringBootTest
public class AudioModelTests {

    private static final Logger log = LoggerFactory.getLogger(AudioModelTests.class);

    @Autowired
    private OpenAiAudioTranscriptionModel transcriptionModel;

    @Test
    public void testAudioModel() {
        Resource resource = new ClassPathResource("/audio/voc_kart_rider.mp3");
        String script = transcriptionModel.call(resource);
        log.info("고객민원 : {}", script);
    }
}
