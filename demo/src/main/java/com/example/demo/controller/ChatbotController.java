package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatbotController {
    private final OpenAiChatModel chatModel;
    private final ChatMemory chatMemory;

    @PostMapping("/api/chats")
    public String getChatSimple(@RequestParam("id") String id, @RequestParam("message") String message) {
        if (chatMemory.get(id).isEmpty()) {
            chatMemory.add(id, SystemMessage.builder().text("명료하고 짧게 답변 해 주세요.").build());
        }
        UserMessage userMessage = new UserMessage(message);
        chatMemory.add(id, userMessage);
        Prompt.builder()
                .messages(chatMemory.get(id))
                .build();
        Prompt prompt = new Prompt(chatMemory.get(id));
        ChatResponse chatResponse = chatModel.call(prompt);
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        chatMemory.add(id, assistantMessage);
        return assistantMessage.getText();
    }
}
