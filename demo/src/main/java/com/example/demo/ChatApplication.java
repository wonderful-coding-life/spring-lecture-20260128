package com.example.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
@RequiredArgsConstructor
@Slf4j
public class ChatApplication implements ApplicationRunner {
    private final OpenAiChatModel chatModel;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // runSimple(args);
        runOptions();
    }

    private void runSimple(ApplicationArguments args) throws Exception {
        // 오직 --message 옵션만 확인 (요청에 따라 위치 인수/시스템 속성 등은 사용하지 않음)
        String message = null;
        if (args.containsOption("message")) {
            List<String> values = args.getOptionValues("message");
            if (values != null && !values.isEmpty()) {
                message = values.getFirst();
            }
        }

        if (message == null || message.isBlank()) {
            message = "스프링부트에 대해 간략하게 소개해 줘.";
            log.info("메시지 인수가 없어 기본 메시지를 사용합니다.");
        } else {
            log.info("실행 인수로 받은 메시지: [REDACTED]");
        }

        String completions = chatModel.call(message);
        log.info("AI 답변: {}", completions);
    }

    private void runOptions() {
        String message = "무지개의 색을 새로운 방식으로 설명해 줘.";

        OpenAiChatOptions openAiChatOptions = OpenAiChatOptions.builder()
                .model(OpenAiApi.ChatModel.GPT_5)
                .N(1)
                .topP(1.0)
                .temperature(1.0)
                .build();

        Prompt prompt = Prompt.builder()
                .chatOptions(openAiChatOptions)
                .content(message).build();


        ChatResponse chatResponse = chatModel.call(prompt);

        for (var generation : chatResponse.getResults()) {
            log.info("AI 답변: {}", generation.getOutput().getText());
        }

        //log.info("AI 답변: {}", chatResponse.getResult().getOutput().getText());
    }
}
