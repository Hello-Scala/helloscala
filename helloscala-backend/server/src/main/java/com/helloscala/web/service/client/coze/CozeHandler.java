package com.helloscala.web.service.client.coze;

import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.helloscala.common.web.exception.FailedDependencyException;
import com.helloscala.web.service.client.coze.response.BotDetailView;
import com.helloscala.web.service.client.coze.response.CozeResponse;
import com.helloscala.web.service.client.coze.response.FileView;
import com.helloscala.web.service.client.coze.response.ListBotResponse;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * @author Steve Zou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CozeHandler {
    @Resource
    private final CozeClient cozeClient;

    public ListBotResponse listBots() {
        ForestResponse<CozeResponse<ListBotResponse>> response = cozeClient.listBots(1, 100);
        return getCozeResponse(response);
    }

    public BotDetailView getBot(String id) {
        ForestResponse<CozeResponse<BotDetailView>> response = cozeClient.getBotDetail(id);
        return getCozeResponse(response);
    }

    @SneakyThrows
    public FileView uploadFile(MultipartFile multipartFile) {
        ForestResponse<CozeResponse<FileView>> response = cozeClient.uploadFile(multipartFile);
        return getCozeResponse(response);
    }
/*

    private static <T> T getCozeResponse(ForestResponse<?> response, Class<T> tClass) {
        if (!response.isSuccess()) {
            Throwable exception = response.getException();
            String msg = Optional.ofNullable(exception).map(Throwable::getMessage).orElse("");
            throw new FailedDependencyException(exception, "Coze api failed, msg={}!", msg);
        }
        CozeResponse<?> result = response.get(CozeResponse.class);

        if (result.getCode() != 0) {
            throw new FailedDependencyException("Coze api failed, msg={}!", result.getMsg());
        }
        String jsonString = JSONObject.toJSONString(result.getData());
        return JSONObject.parseObject(jsonString, tClass);
    }*/

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
