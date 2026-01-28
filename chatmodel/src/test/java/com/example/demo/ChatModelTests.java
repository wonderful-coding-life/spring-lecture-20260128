package com.example.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
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

    @Test
    public void testChatModelMessageContext() {
        Message system = new SystemMessage("간략하게 답변해 주세요.");
        Message message1 = new UserMessage("서울 올림픽에 대해 알려 주세요");
        Message assistant1 = new AssistantMessage("서울 올림픽, 공식명칭은 제24회 하계 올림픽대회는 1988년 9월 17일부터 10월 2일까지 대한민국 서울에서 개최되었습니다. 이 대회는 한국에서 처음으로 열린 올림픽 경기로, 총 159개 국가가 참여하였고 23종목의 경기가 진행되었습니다. 서울 올림픽은 성공적인 개최로 평가받았으며, 그로 인해 한국은 국제 스포츠 무대에서의 위상이 크게 향상되었습니다. 또한, 대회 기간 동안 평화와 화합을 강조하며 많은 이들에게 기억에 남는 현장이 되었습니다.");
        Message message2 = new UserMessage("그럼 바로 그 이전 올림픽은 어디야?");
        Message assistant2 = new AssistantMessage("바로 이전 올림픽은 1984년 하계 올림픽으로, 미국 로스앤젤레스에서 개최되었습니다. 이 대회는 7월 28일부터 8월 12일까지 진행되었고, 많은 국가가 참여한 강행된 올림픽 중 하나였습니다.");
        Message message3 = new UserMessage("그럼 그 두개의 올림픽중 참여 국가는 어디가 많아?");

        String result = chatModel.call(system, message1, assistant1, message2, assistant2, message3);
        log.info("result {}", result);
    }
}
