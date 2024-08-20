package com.helloscala.web.service.client.coze;

import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.http.ForestResponse;
import com.helloscala.common.web.exception.FailedDependencyException;
import com.helloscala.web.service.client.coze.request.*;
import com.helloscala.web.service.client.coze.response.*;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.Disposable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Steve Zou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CozeHandler {
    @Resource
    private final CozeClient cozeClient;
    private final CozeStreamClient cozeStreamClient;

    public ListBotResponse listBots() {
        ForestResponse<CozeResponse<ListBotResponse>> response = cozeClient.listBots(1, 100);
        return getCozeResponse(response);
    }

    public BotDetailView getBot(String id) {
        ForestResponse<CozeResponse<BotDetailView>> response = cozeClient.getBotDetail(id);
        return getCozeResponse(response);
    }

    public ConversationView createConversation() {
        CreateConversationRequest request = new CreateConversationRequest();
        ForestResponse<CozeResponse<ConversationView>> response = cozeClient.createConversation(request);
        return getCozeResponse(response);
    }

    public List<MsgView> listChatMsgs(String conversationId, String chatId) {
        ForestResponse<CozeResponse<List<MsgView>>> forestResponse = cozeClient.listChatMsg(conversationId, chatId);
        return getCozeResponse(forestResponse);
    }

    public ChatView chat(String assistantId, String userId, String conversationId, ConversationMsgView msgView) {
        CreateChatRequest createChatRequest = new CreateChatRequest();
        createChatRequest.setBotId(assistantId);
        createChatRequest.setUserId(userId);
        createChatRequest.setStream(false);
        createChatRequest.setAutoSaveHistory(true);
        createChatRequest.setAdditionalMsgs(List.of(msgView));
        String request = JSONObject.toJSONString(createChatRequest);
        ForestResponse<CozeResponse<ChatView>> forestResponse = cozeClient.createChat(conversationId, request);
        return getCozeResponse(forestResponse);
    }

    public Disposable streamingChat(String assistantId, String userId, String conversationId, ConversationMsgView msgView, Consumer<ServerSentEvent> consumer) {
        CreateChatRequest createChatRequest = new CreateChatRequest();
        createChatRequest.setBotId(assistantId);
        createChatRequest.setUserId(userId);
        createChatRequest.setStream(true);
        createChatRequest.setAutoSaveHistory(true);
        createChatRequest.setAdditionalMsgs(List.of(msgView));

        return cozeStreamClient.createChat(conversationId, createChatRequest)
                .doOnNext(consumer).subscribe();
    }

    @SneakyThrows
    public FileView uploadFile(MultipartFile multipartFile) {
        ForestResponse<CozeResponse<FileView>> response = cozeClient.uploadFile(multipartFile);
        return getCozeResponse(response);
    }

    private static <T> T getCozeResponse(ForestResponse<CozeResponse<T>> response) {
        if (!response.isSuccess()) {
            Throwable exception = response.getException();
            String msg = Optional.ofNullable(exception).map(Throwable::getMessage).orElse("");
            throw new FailedDependencyException(exception, "Coze api failed, msg={}!", msg);
        }
        CozeResponse<T> cozeResponse = response.getResult();
        if (cozeResponse.getCode() != 0) {
            throw new FailedDependencyException("Coze api failed, msg={}!", cozeResponse.getMsg());
        }
        return cozeResponse.getData();
    }
}
