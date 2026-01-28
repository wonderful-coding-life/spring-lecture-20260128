package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

@Component
@Slf4j
public class ChatApplication implements ApplicationRunner {
    @Autowired
    private OpenAiChatModel chatModel;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // testImageModal();
        testAudioModal();
    }

    private void testImageModal() {
        log.info("커맨드라인 방식의 애플리케이션 동작...");

        var resource = new ClassPathResource("/image/Disney_World_1.jpg");
//        var resource1 = new FileSystemResource("D:\\arichive\\image\\test.jpg");
//        var resource2 = new UrlResource("http://temp.co.kr/image/test.png");

        var media = Media.builder()
                .data(resource)
                .mimeType(MimeTypeUtils.IMAGE_JPEG)
                .build();
        var userMessage = UserMessage.builder()
                .text("사진속의 풍경을 멋진 시로 써 주세요.")
                .media(media)
                .build();

        var completions = chatModel.call(userMessage);
        log.info("AI 응답 {}", completions);
    }

    private void testAudioModal() {
        var resource = new ClassPathResource("/audio/sample_audio_springai.mp3");
        var media = Media.builder()
                .data(resource)
                .mimeType(MimeTypeUtils.parseMimeType("audio/mp3"))
                .build();
        var options = OpenAiChatOptions.builder()
                .model("gpt-audio-mini")
                .build();
        var message = UserMessage.builder()
                .media(media)
                .text("이 오디오 파일의 내용을 일본어로 요약해 주세요.")
                .build();
        var response = chatModel.call(new Prompt(message, options));
        log.info("AI 응답 결과 {}", response.getResult().getOutput().getText());
    }
}
