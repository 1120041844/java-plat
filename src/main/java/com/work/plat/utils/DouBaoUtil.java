package com.work.plat.utils;

import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DouBaoUtil {

    private static String apiKey = "31ef9506-4f7d-4a55-a1ea-9f4caa45e566";
    private static ArkService service;
    static {
        service = ArkService.builder().apiKey(apiKey).build();
    }

    public static void main(String[] args) {
        streamingChat();
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
                        choice -> {
                            if (choice.getChoices().size() > 0) {
                                System.out.print(choice.getChoices().get(0).getMessage().getContent());
                            }
                        }
                );

        // shutdown service
        service.shutdownExecutor();
    }
}
