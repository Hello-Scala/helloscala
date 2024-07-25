package com.helloscala.web.service.client.coze;

import com.dtflys.forest.annotation.*;
import com.dtflys.forest.backend.ContentType;
import com.dtflys.forest.http.ForestResponse;
import com.helloscala.web.service.client.ForestHttpMethod;
import com.helloscala.web.service.client.coze.request.*;
import com.helloscala.web.service.client.coze.response.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * @author Steve Zou
 */
@ForestClient
@BaseRequest(baseURL = "{cozeBaseUrl}")
public interface CozeClient {
    @Request(type = ForestHttpMethod.GET,
            url = "/v1/bot/get_online_info",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CozeResponse<BotDetailView>> getBotDetail(@Query("bot_id") String id);

    @Request(type = ForestHttpMethod.GET,
            url = "/v1/space/published_bots_list?space_id={cozeSpaceId}",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CozeResponse<ListBotResponse>> listBots(@Query(name = "page_index") Integer pageIndex,
                                                           @Query(name = "page_size") Integer pageSize);

    @Request(type = ForestHttpMethod.POST,
            url = "/v1/conversation/create",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CozeResponse<ConversationView>> createConversation(@Body CreateConversationRequest request);

    @Request(type = ForestHttpMethod.GET,
            url = "/v1/conversation/retrieve",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CozeResponse<ConversationView>> getConversation(@Query(name = "conversation_id") String id);

    @Request(type = ForestHttpMethod.POST,
            url = "/v1/conversation/message/create",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CozeResponse<MsgView>> createMessage(@Query(name = "conversation_id") String id,
                                                             @Body CreateMsgRequest request);

    @Request(type = ForestHttpMethod.POST,
            url = "/v1/conversation/message/list",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<ListMsgResponse> listMsg(@Query(name = "conversation_id") String conversationId,
                                            @Body ListMsgRequest request);

    @Request(type = ForestHttpMethod.GET,
            url = "/v1/conversation/message/retrieve",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CozeResponse<MsgView>> getMsg(@Query(name = "conversation_id") String conversationId,
                                                 @Query(name = "message_id") String message);

    @Request(type = ForestHttpMethod.POST,
            url = "/v1/conversation/message/retrieve",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CozeResponse<MsgView>> modifyMsg(@Query(name = "conversation_id") String conversationId,
                                                    @Query(name = "message_id") String message,
                                                    @Body ModifyMsgRequest request);

    @Request(type = ForestHttpMethod.POST,
            url = "/v3/chat",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CozeResponse<ChatView>> createChat(@Query(name = "conversation_id") String conversationId,
                                                      @Body CreateChatRequest request);

    @Request(type = ForestHttpMethod.POST,
            url = "/v3/chat/retrieve",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CozeResponse<ChatView>> getChatDetail(@Query(name = "conversation_id") String conversationId,
                                                         @Query(name = "chat_id") String chatId);

    @Request(type = ForestHttpMethod.POST,
            url = "/v3/chat/message/list",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CozeResponse<List<MsgView>>> listChatMsg(@Query(name = "conversation_id") String conversationId,
                                                            @Query(name = "chat_id") String chatId);

    @Request(type = ForestHttpMethod.POST,
            url = "/v3/chat/submit_tool_outputs",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CozeResponse<ChatView>> submitToolOutputs(@Query(name = "conversation_id") String conversationId,
                                                             @Query(name = "chat_id") String chatId,
                                                             @Body SubmitToolOutputsRequest request);

    @Request(type = ForestHttpMethod.POST,
            url = "/v1/files/upload",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CozeResponse<FileView>> uploadFile(@DataFile("file") MultipartFile file);

    @Request(type = ForestHttpMethod.GET,
            url = "/v1/files/retrieve",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CozeResponse<FileView>> getFile(@Query(name = "file_id") String id);

    @Request(type = ForestHttpMethod.POST,
            url = "/open_api/knowledge/document/create",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CreateKnowledgeFileResponse> createKnowledgeDocument(@Body CreateKnowledgeFileRequest request);

    @Request(type = ForestHttpMethod.POST,
            url = "/open_api/knowledge/document/update",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CozeResponse<Object>> updateKnowledgeDocument(@Body UpdateKnowledgeFileRequest request);

    @Request(type = ForestHttpMethod.POST,
            url = "/open_api/knowledge/document/delete",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CozeResponse<Object>> deleteKnowledgeDocument(@Body DeleteKnowledgeFileRequest request);

    @Request(type = ForestHttpMethod.POST,
            url = "/open_api/knowledge/document/list",
            headers = {"Authorization: Bearer {cozeAccessToken}"})
    ForestResponse<CozeResponse<Object>> listKnowledgeDocument(@Body ListKnowledgeFileRequest request);
}
