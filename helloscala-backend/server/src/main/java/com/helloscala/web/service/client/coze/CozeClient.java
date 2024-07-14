package com.helloscala.web.service.client.coze;

import com.dtflys.forest.annotation.*;
import com.dtflys.forest.http.ForestResponse;
import com.helloscala.web.service.client.ForestHttpMethod;
import com.helloscala.web.service.client.coze.request.CreateConversationRequest;
import com.helloscala.web.service.client.coze.request.CreateMsgRequest;
import com.helloscala.web.service.client.coze.request.ListMsgRequest;
import com.helloscala.web.service.client.coze.request.ModifyMsgRequest;
import com.helloscala.web.service.client.coze.response.*;

import java.util.List;

/**
 * @author Steve Zou
 */
@ForestClient
@BaseRequest(baseURL = "{cozeBaseUrl}")
public interface CozeClient {
    @Request(type = ForestHttpMethod.GET,
            url = "/bot/get_online_info?bot_id={cozeBotId}",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CozeResponse<List<BotDetailView>>> getBotDetail();

    @Request(type = ForestHttpMethod.GET,
            url = "/space/published_bots_list?space_id={cozeSpaceId}",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CozeResponse<ListBotResponse>> listBots(@Query(name = "page_index") Integer pageIndex,
                                                           @Query(name = "page_size") Integer pageSize);

    @Request(type = ForestHttpMethod.POST,
            url = "/conversation/create",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CozeResponse<ConversationView>> createConversation(@Body CreateConversationRequest request);

    @Request(type = ForestHttpMethod.GET,
            url = "/conversation/retrieve",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CozeResponse<ConversationView>> getConversation(@Query(name = "conversation_id") String id);

    @Request(type = ForestHttpMethod.POST,
            url = "/conversation/message/create",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CozeResponse<MsgView>> createConversation(@Query(name = "conversation_id") String id,
                                                             @Body CreateMsgRequest request);

    @Request(type = ForestHttpMethod.POST,
            url = "/conversation/message/list",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<ListMsgResponse> listMsg(@Query(name = "conversation_id") String conversationId,
                                            @Body ListMsgRequest request);

    @Request(type = ForestHttpMethod.GET,
            url = "/conversation/message/retrieve",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CozeResponse<MsgView>> getMsg(@Query(name = "conversation_id") String conversationId,
                                   @Query(name = "message_id") String message);

    @Request(type = ForestHttpMethod.POST,
            url = "/conversation/message/retrieve",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CozeResponse<MsgView>> modifyMsg(@Query(name = "conversation_id") String conversationId,
                                      @Query(name = "message_id") String message,
                                      @Body ModifyMsgRequest request    );
}
