package com.example.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.MimeTypeUtils;

@SpringBootTest
public class ChatModelTests {

    private static final Logger log = LoggerFactory.getLogger(ChatModelTests.class);

    @Autowired
    private OpenAiChatModel chatModel;

    @Test
    public void testMultiModal() {

        Resource resource = new ClassPathResource("/images/Disney_World_1.jpg");
        //new FileSystemResource("D:\\archive\\image\\test.jpg");
        //new UrlResource("https://image.org/test.jpg");
        Media media = Media.builder()
                .mimeType(MimeTypeUtils.IMAGE_JPEG)
                .data(resource)
                .build();

        UserMessage message = UserMessage.builder()
                .media(media)
                .text("사진속의 풍경을 멋진 시로 써 주세요. 초등학생의 말투와 수준으로 써 주세요.").build();

        String completions = chatModel.call(message);
        log.info("AI 답변: {}", completions);
    }

}
