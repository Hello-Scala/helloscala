package com.helloscala.web.service.client.coze;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.helloscala.web.config.CozeConfiguration;
import com.helloscala.web.service.client.coze.request.CreateChatRequest;
import com.helloscala.web.service.client.coze.response.StreamResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author Steve Zou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CozeStreamClient {
    private final CozeConfiguration cozeConfig;
    public static final String CHAT_URL = "/v3/chat";
    private WebClient webClient;

    @PostConstruct
    public void init() {
        if (Objects.isNull(webClient)) {
            webClient = WebClient.create(cozeConfig.getBaseUrl());
        }
    }

    Flux<ServerSentEvent> createChat(String conversationId,
                                    CreateChatRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", StrUtil.format("Bearer {}", cozeConfig.getAccessToken()));
        String uri = StrUtil.isBlank(conversationId) ? CHAT_URL : CHAT_URL + "?conversation_id=" + conversationId;
        return webClient.post().uri(uri)
                .headers(h -> h.addAll(headers))
                .body(Flux.just(JSONObject.toJSONString(request)), String.class)
                .retrieve()
                .bodyToFlux(ServerSentEvent.class);
    }
}
