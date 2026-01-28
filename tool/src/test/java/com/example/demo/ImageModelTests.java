package com.example.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@SpringBootTest
public class ImageModelTests {
    private static final Logger log = LoggerFactory.getLogger(ImageModelTests.class);

    @Autowired
    private OpenAiImageModel imageModel;

    @Test
    public void testImageGeneration() {
        String message = """
            화성 표면에서 탐사 로버가 움직이고 있으며, 그 옆에는 2족 보행 로봇이 함께 탐사 활동을 하고 있다.
            붉은 모래 언덕과 먼지 낀 하늘이 배경이며, 태양빛이 낮게 비추는 오후의 분위기.
            실제 사진처럼 보이는 고해상도 장면, 자연스러운 그림자와 질감.
            """;

        ImageResponse response = imageModel.call(new ImagePrompt(message));
        log.info("{}", response.getResult().getOutput().getUrl());
    }

    @Test
    public void testImageOptions() throws IOException {
        String message = """
            화성 표면에서 탐사 로버가 움직이고 있으며, 그 옆에는 2족 보행 로봇이 함께 탐사 활동을 하고 있다.
            붉은 모래 언덕과 먼지 낀 하늘이 배경이며, 태양빛이 낮게 비추는 오후의 분위기.
            실제 사진처럼 보이는 고해상도 장면, 자연스러운 그림자와 질감.
            """;

        OpenAiImageOptions openAiImageOptions = OpenAiImageOptions.builder()
                .model("dall-e-3") // dall-e-3, gpt-image-1-mini (protected - image input)
                .style("natural") // vivid (default), natural
                .quality("hd") // standard (default), hd
                .responseFormat("b64_json") // url (default), b64_json
                .width(1024)
                .height(1024).build();

        ImagePrompt imagePrompt = new ImagePrompt(message, openAiImageOptions);
        ImageResponse imageResponse = imageModel.call(imagePrompt);

        if (imageResponse.getResult().getOutput().getB64Json() != null) {
            // OpenAI와 통신은 JSON으로 이루어지고, JSON 내에서 바이너리를 표현하기 위해 Base64 인코딩을 함
            log.info("Base64Json {}", imageResponse.getResult().getOutput().getB64Json());
            byte[] imageBytes = Base64.getDecoder().decode(imageResponse.getResult().getOutput().getB64Json());
            // dall-e-3는 png 포맷만 지원, 다른 포맷이 필요하다면 애플리케이션에서 변환해야 함
            Files.write(Paths.get("D:\\archive\\image\\openai-image.png"), imageBytes);
        }

        if (imageResponse.getResult().getOutput().getUrl() != null) {
            log.info("Url {}", imageResponse.getResult().getOutput().getUrl());
        }
    }

}
