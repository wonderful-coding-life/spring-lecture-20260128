package com.example.demo;

import com.example.demo.model.Actor;
import com.example.demo.model.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.text.MessageFormat;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class StructuredOutputConverterTests {
    @Autowired
    private OpenAiChatModel openAiChatModel;

    @Test
    public void testConverter() {
        // Converter는 LLM의 응답을 자바 객체로 만들어 준다
        BeanOutputConverter<Actor> beanOutputConverter = new BeanOutputConverter<>(Actor.class);

        // 프롬프트 구성 예
        String format = beanOutputConverter.getFormat();
        String actor = "브래드 피트";

        String message = MessageFormat.format("""
            {0}의 최신 출연작 5편의 영화를 알려 줘. 특히 2025년에 나온 영화만 포함해 줘.
            {1}
        """, actor, format);

        String result = openAiChatModel.call(message);

        Actor actorMovies = beanOutputConverter.convert(result);
        assertThat(actorMovies).isNotNull();

        System.out.println("배우: " + actorMovies.getActor());
        for (Movie movie : actorMovies.getMovies()) {
            System.out.println("영화: " + movie);
        }
    }
}
