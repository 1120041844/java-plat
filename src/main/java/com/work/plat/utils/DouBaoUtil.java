package com.work.plat.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.alibaba.fastjson.JSON;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionChunk;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
@Slf4j
public class DouBaoUtil {

    private static String apiKey = "31ef9506-4f7d-4a55-a1ea-9f4caa45e566";
    private static ArkService service;
    static {
        service = ArkService.builder().apiKey(apiKey).build();
    }


    public static void simpleChat() {
        System.out.println("\n----- standard request -----");
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content("你是豆包，是由字节跳动开发的 AI 人工智能助手").build();
        final ChatMessage userMessage = ChatMessage.builder().role(ChatMessageRole.USER).content("常见的十字花科植物有哪些？").build();
        messages.add(systemMessage);
        messages.add(userMessage);

        ChatCompletionRequest chatCompletionRequest =
                ChatCompletionRequest.builder()
                        .model("ep-20241019145904-z8xbc")
                        .messages(messages)
                        .build();

        service.createChatCompletion(chatCompletionRequest).getChoices().forEach(choice -> System.out.println(choice.getMessage().getContent()));

        // shutdown service
        service.shutdownExecutor();
    }

    public static void multipleRoundsChat() {
        System.out.println("\n----- multiple rounds request -----");
        final List<ChatMessage> messages = Arrays.asList(
                ChatMessage.builder().role(ChatMessageRole.SYSTEM).content("你是豆包，是由字节跳动开发的 AI 人工智能助手").build(),
                ChatMessage.builder().role(ChatMessageRole.USER).content("花椰菜是什么？").build(),
                ChatMessage.builder().role(ChatMessageRole.ASSISTANT).content("花椰菜又称菜花、花菜，是一种常见的蔬菜。").build(),
                ChatMessage.builder().role(ChatMessageRole.USER).content("再详细点").build()
        );

        ChatCompletionRequest chatCompletionRequest =
                ChatCompletionRequest.builder()
                        .model("ep-20241019145904-z8xbc")
                        .messages(messages)
                        .build();

        service.createChatCompletion(chatCompletionRequest).getChoices().forEach(choice -> System.out.println(choice.getMessage().getContent()));
        // shutdown service
        service.shutdownExecutor();
    }

    public static void streamingChat() {
        System.out.println("\n----- streaming request -----");
        final List<ChatMessage> streamMessages = new ArrayList<>();
        final ChatMessage streamSystemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content("你是英语老师").build();
        final ChatMessage streamUserMessage = ChatMessage.builder().role(ChatMessageRole.USER).content("常见的十字花科植物有哪些？").build();
        streamMessages.add(streamSystemMessage);
        streamMessages.add(streamUserMessage);

        ChatCompletionRequest streamChatCompletionRequest = ChatCompletionRequest.builder()
                .model("ep-20241019145904-z8xbc")
                .messages(streamMessages)
                .build();

        service.streamChatCompletion(streamChatCompletionRequest)
                .doOnError(Throwable::printStackTrace)
                .blockingForEach(
                        new Consumer<ChatCompletionChunk>() {
                            @Override
                            public void accept(ChatCompletionChunk choice) throws Exception {
                                if (choice.getChoices().size() > 0) {
                                    System.out.print(choice.getChoices().get(0).getMessage().getContent());
                                }
                            }
                        }
                );

        // shutdown service
        service.shutdownExecutor();
    }

    public static Flowable<ChatCompletionChunk> streamingChat2(Map<String,String> historyMap,String roleKey, String message) {
        final List<ChatMessage> streamMessages = new ArrayList<>();
        final ChatMessage streamSystemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content(roleKey).build();
        streamMessages.add(streamSystemMessage);
        // 是否关联历史
        if (CollUtil.isNotEmpty(historyMap)) {
            for (String question : historyMap.keySet()) {
                String answer = historyMap.get(question);
                ChatMessage questionMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(question).build();
                streamMessages.add(questionMessage);

                ChatMessage answerMessage = ChatMessage.builder().role(ChatMessageRole.ASSISTANT).content(answer).build();
                streamMessages.add(answerMessage);
            }
        }
        // 问题
        final ChatMessage streamUserMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(message).build();
        streamMessages.add(streamUserMessage);

        ChatCompletionRequest streamChatCompletionRequest = ChatCompletionRequest.builder()
                .model("ep-20241019145904-z8xbc")
                .messages(streamMessages)
                .build();

        Flowable<ChatCompletionChunk> completion = service.streamChatCompletion(streamChatCompletionRequest);
        return completion;

    }

    public static void main(String[] args) {
        Flowable<ChatCompletionChunk> chatCompletionChunkFlowable = streamingChat2(null, "null", "吃饭了么");
        chatCompletionChunkFlowable.doOnError(throwable -> {
        }).doOnComplete(() -> {
        }).blockingForEach(
                choice -> {
                    if (choice.getChoices().size() > 0) {
                        Object content = choice.getChoices().get(0).getMessage().getContent();
                        String message = String.valueOf(content);
                        System.out.print(message);
                    }
                });
    }
}
