package com.example.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.metadata.RateLimit;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChatModelTests {

    private static final Logger log = LoggerFactory.getLogger(ChatModelTests.class);

    @Autowired
    private OpenAiChatModel chatModel;

    @Test
    public void testChatOptions() {
        String message = "무지개의 색을 새로운 방식으로 설명해줘.";

        var options = OpenAiChatOptions.builder()
                .model("chatgpt-4o-latest")
                .N(2)
                .topP(1.0)
                .temperature(1.3)
                .build();

        var prompt = new Prompt(message, options);
        var response = chatModel.call(prompt);

           log.info("model {}", response.getMetadata().getModel());
        for (Generation generation : response.getResults()) {
            log.info("response {}", generation.getOutput().getText());
        }

        Usage usage = response.getMetadata().getUsage();
        log.info("promptTokens {}, completionTokens {}, totalTokens {}",
                usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens());
        RateLimit rateLimit = response.getMetadata().getRateLimit();
        log.info("requestLimit {}, requestRemaining {}, requestReset {}",
                rateLimit.getRequestsLimit(), rateLimit.getRequestsRemaining(), rateLimit.getRequestsReset());
        log.info("tokensLimit {}, tokensRemaining {}, tokensReset {}",
                rateLimit.getTokensLimit(), rateLimit.getTokensRemaining(), rateLimit.getTokensReset());
    }
}
