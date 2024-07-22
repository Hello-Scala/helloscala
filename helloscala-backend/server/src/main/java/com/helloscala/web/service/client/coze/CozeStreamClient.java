package com.helloscala.web.service.client.coze;

import com.dtflys.forest.annotation.*;
import com.dtflys.forest.http.ForestResponse;
import com.helloscala.web.service.client.ForestHttpMethod;
import com.helloscala.web.service.client.coze.request.*;
import com.helloscala.web.service.client.coze.response.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Steve Zou
 */
@Slf4j
@Service
public class CozeStreamClient {
    @Request(type = ForestHttpMethod.POST,
            url = "/v3/chat",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CozeResponse<ChatView>> createChat(@Query(name = "conversation_id") String conversationId,
                                                      @Body CreateChatRequest request) {
        return null;
    }
}
