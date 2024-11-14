package com.work.ai.service;

import com.volcengine.ark.runtime.model.completion.chat.*;
import com.volcengine.ark.runtime.service.ArkService;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DouBaoService {

    private static String apiKey = "31ef9506-4f7d-4a55-a1ea-9f4caa45e566";
    private static String model = "ep-20241019145904-z8xbc";
    private ArkService service;

    public static DouBaoService init() {
        DouBaoService douBaoService = new DouBaoService();
        douBaoService.service = ArkService.builder().apiKey(apiKey).build();

        return douBaoService;
    }

    public List<ChatCompletionChoice> simpleChat(String question) {
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content("你是豆包，是由字节跳动开发的 AI 人工智能助手").build();
        final ChatMessage userMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(question).build();
        messages.add(systemMessage);
        messages.add(userMessage);

        ChatCompletionRequest chatCompletionRequest =
                ChatCompletionRequest.builder()
                        .model(model)
                        .messages(messages)
                        .build();
        return service.createChatCompletion(chatCompletionRequest).getChoices();
    }

    public List<ChatCompletionChoice> multipleRoundsChat(String... questions) {
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(ChatMessage.builder().role(ChatMessageRole.SYSTEM).content("你是豆包，是由字节跳动开发的 AI 人工智能助手").build());
        for (String question : questions) {
            messages.add(ChatMessage.builder().role(ChatMessageRole.USER).content(question).build());
        }

        ChatCompletionRequest chatCompletionRequest =
                ChatCompletionRequest.builder()
                        .model(model)
                        .messages(messages)
                        .build();
        return service.createChatCompletion(chatCompletionRequest).getChoices();

    }

    public Flowable<ChatCompletionChunk> streamingChat() {
        final List<ChatMessage> streamMessages = new ArrayList<>();
        final ChatMessage streamSystemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content("你是豆包，是由字节跳动开发的 AI 人工智能助手").build();
        streamMessages.add(streamSystemMessage);

        ChatCompletionRequest streamChatCompletionRequest = ChatCompletionRequest.builder()
                .model(model)
                .messages(streamMessages)
                .build();

        return service.streamChatCompletion(streamChatCompletionRequest)
                .doOnError(Throwable::printStackTrace);
    }


}
