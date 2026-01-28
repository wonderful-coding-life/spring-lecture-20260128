package com.example.demo.controller;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @Autowired
    private OpenAiChatModel chatModel;

    @Autowired
    private ChatMemory chatMemory;

    @PostMapping("/api/chats")
    public String postChats(@RequestParam("id") String id, @RequestParam("message") String message) {
        if (chatMemory.get(id).isEmpty()) {
            chatMemory.add(id, new SystemMessage("정확하고 명료하고 그리고 짧게 답변 해 주세요."));
        }

        var userMessage = new UserMessage(message);
        chatMemory.add(id, userMessage);

        var prompt = new Prompt(chatMemory.get(id));
        var response = chatModel.call(prompt);

        var assistantMessage = response.getResult().getOutput();
        chatMemory.add(id, assistantMessage);
        return assistantMessage.getText();
    }
}
