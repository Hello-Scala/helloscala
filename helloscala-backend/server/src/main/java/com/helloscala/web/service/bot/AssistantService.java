package com.helloscala.web.service.bot;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.entity.AssistantConversation;
import com.helloscala.common.entity.AssistantMessage;
import com.helloscala.common.entity.CozeFile;
import com.helloscala.common.enums.ContentTypeEnum;
import com.helloscala.common.enums.MsgTypeEnum;
import com.helloscala.common.enums.SendFromEnum;
import com.helloscala.common.service.AssistantConversationService;
import com.helloscala.common.service.AssistantMessageService;
import com.helloscala.common.service.CozeFileService;
import com.helloscala.common.service.FileService;
import com.helloscala.common.utils.DateHelper;
import com.helloscala.common.utils.ListHelper;
import com.helloscala.common.vo.user.SystemUserVO;
import com.helloscala.common.web.exception.BadRequestException;
import com.helloscala.common.web.exception.NotFoundException;
import com.helloscala.web.controller.coze.request.ChatWithAssistantRequest;
import com.helloscala.web.controller.coze.request.MessageView;
import com.helloscala.web.controller.coze.response.*;
import com.helloscala.web.service.client.coze.CozeHandler;
import com.helloscala.web.service.client.coze.request.*;
import com.helloscala.web.service.client.coze.response.*;
import com.helloscala.web.websocket.AssistantChatWebSocket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * @author Steve Zou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AssistantService {
    private final CozeHandler cozeHandler;
    private final FileService fileService;
    private final CozeFileService cozeFileService;
    private final AssistantConversationService conversationService;
    private final AssistantMessageService messageService;
    private final AssistantChatWebSocket chatWebSocket;

    @Cacheable(value = "assistant:list")
    public ListAssistantResponse list() {
        ListBotResponse listBotResponse = cozeHandler.listBots();
        return buildListAssistantResponse(listBotResponse);
    }

    @Cacheable(value = "assistant:", key = "'entity_' + #id")
    public GetAssistantResponse get(String id) {
        BotDetailView botDetailView = cozeHandler.getBot(id);
        return buildGetAssistantResponse(botDetailView);
    }

    public ListConversationResponse listConversation(String assistantId, Integer pageNo, Integer pageSize) {
        Page<AssistantConversation> conversationPage = conversationService.list(assistantId, pageNo, pageSize);
        List<BOConversationView> conversationViews = conversationPage.getRecords().stream().map(conversation -> {
            BOConversationView conversationView = new BOConversationView();
            conversationView.setId(conversation.getId());
            conversationView.setBotId(conversation.getBotId());
            conversationView.setSummary(conversation.getSummary());
            conversationView.setLastSendTime(conversation.getLastSendTime());
            conversationView.setCreateTime(conversation.getCreateTime());
            return conversationView;
        }).toList();

        ListConversationResponse response = new ListConversationResponse();
        response.setTotal((int) conversationPage.getTotal());
        response.setCurrentPage((int) conversationPage.getCurrent());
        response.setTotalPage((int) conversationPage.getPages());
        response.setConversations(conversationViews);
        return response;
    }

    public ListConversationMsgResponse listConversationMsg(String conversationId, String createTimeEnd, Integer pageNo, Integer pageSize) {
        AssistantConversation conversation = conversationService.getById(conversationId);
        if (Objects.isNull(conversation)) {
            throw new NotFoundException("Conversation not found, id={}!", conversationId);
        }
        Page<AssistantMessage> page = Page.of(pageNo, pageSize);
        Page<AssistantMessage> assistantMessagePage = messageService.listByConversation(page, conversation.getId(), createTimeEnd);
        List<AssistantMessage> assistantMessages = assistantMessagePage.getRecords();
        List<AssistantMsgView> msgViews = assistantMessages.stream().map(msg -> {
            AssistantMsgView msgView = new AssistantMsgView();
            msgView.setId(msg.getId());
            msgView.setConversationId(msg.getConversationId());
            msgView.setBotId(msg.getBotId());
            msgView.setEvent(AssistantMsgEventEnum.MESSAGE_COMPLETED);
            msgView.setSendFrom(msg.getSendFrom());
            msgView.setContent(msg.getContent());
            msgView.setContentType(msg.getContentType());
            msgView.setUserId(msg.getUserId());
            msgView.setCreateTime(msg.getCreateTime());
            return msgView;
        }).toList();

        ListConversationMsgResponse response = new ListConversationMsgResponse();
        response.setCurrentPage((int) assistantMessagePage.getCurrent());
        response.setTotalPage((int) assistantMessagePage.getPages());
        response.setMsgs(msgViews);
        response.setTotal((int) assistantMessagePage.getTotal());
        return response;
    }

    public ChatWithAssistantResponse chat(GetAssistantResponse assistant, SystemUserVO currentUserInfo, ChatWithAssistantRequest request) {
        String id = assistant.getId();
        AssistantConversation assistantConversation = conversationService.getById(request.getConversationId());
        MessageView msg = request.getMsg();
        if (StrUtil.isBlank(request.getConversationId())) {
            String content = msg.getContent();
            String summary = content.length() < 50 ? content : StrUtil.subPre(content, 50) + "...";
            ConversationView conversation = cozeHandler.createConversation();
            assistantConversation = buildNewAssistantConversation(currentUserInfo, id, conversation, summary);
            conversationService.save(assistantConversation);
        }
        chat(assistant, currentUserInfo, msg, assistantConversation, id);
        ChatWithAssistantResponse assistantResponse = new ChatWithAssistantResponse();
        assistantResponse.setId(assistantConversation.getId());
        assistantResponse.setConversationId(assistantConversation.getConversationId());
        return assistantResponse;
    }

    @NotNull
    private static AssistantConversation buildNewAssistantConversation(SystemUserVO currentUserInfo, String id, ConversationView conversation, String summary) {
        AssistantConversation assistantConversation = new AssistantConversation();
        assistantConversation.setBotId(id);
        assistantConversation.setConversationId(conversation.getId());
        assistantConversation.setUserId(currentUserInfo.getId());
        assistantConversation.setSummary(summary);
        assistantConversation.setCreateTime(CozeHelper.toDate(conversation.getCreatedAt()));
        assistantConversation.setCreateBy(currentUserInfo.getUsername());
        return assistantConversation;
    }

    public ChatWithAssistantResponse streamingChat(GetAssistantResponse assistant, SystemUserVO currentUserInfo, ChatWithAssistantRequest request) {
        String id = assistant.getId();
        AssistantConversation assistantConversation = conversationService.getById(request.getConversationId());
        MessageView msg = request.getMsg();
        if (Objects.isNull(assistantConversation)) {
            String content = msg.getContent();
            String summary = content.length() < 50 ? content : StrUtil.subPre(content, 50) + "...";
            ConversationView conversation = cozeHandler.createConversation();
            assistantConversation = buildNewAssistantConversation(currentUserInfo, id, conversation, summary);
            conversationService.save(assistantConversation);
        }
        streamingChat(assistant, currentUserInfo, assistantConversation, msg);
        ChatWithAssistantResponse assistantResponse = new ChatWithAssistantResponse();
        assistantResponse.setId(assistantConversation.getId());
        assistantResponse.setConversationId(assistantConversation.getConversationId());
        return assistantResponse;
    }

    @Async
    public void chat(GetAssistantResponse assistant, SystemUserVO currentUserInfo, MessageView msg, AssistantConversation conversation, String id) {
        String msgContent = buildMsgContent(msg.getMsgType(), msg.getContent());
        ConversationMsgView msgView = new ConversationMsgView();
        msgView.setRole(RoleEnum.USER);
        msgView.setContent(msgContent);
        msgView.setContentType(toContentType(msg.getMsgType()));
        saveAndSendUserMsg(currentUserInfo, assistant, conversation.getId(), msg, msgView);
        ChatView chatView = cozeHandler.chat(id, currentUserInfo.getId(), conversation.getConversationId(), msgView);
        log.info("Created chat: {}", JSONObject.toJSONString(chatView));
        List<MsgView> msgViews = cozeHandler.listChatMsgs(conversation.getConversationId(), chatView.getId());
        msgViews.forEach(chatMsg -> {
            saveAndSendAssistantMsg(assistant, currentUserInfo, conversation.getId(), chatMsg);
        });
    }

    private void saveAndSendAssistantMsg(GetAssistantResponse assistant, SystemUserVO currentUserInfo, String msgConversation, MsgView chatMsg) {
        if (MessageTypeEnum.ANSWER == chatMsg.getType()) {
            AssistantMessage assistantMessage = new AssistantMessage();
            assistantMessage.setConversationId(msgConversation);
            assistantMessage.setBotId(assistant.getId());
            assistantMessage.setMessageId(chatMsg.getId());
            assistantMessage.setSendFrom(SendFromEnum.ASSISTANT);
            assistantMessage.setContent(chatMsg.getContent());
            assistantMessage.setContentType(ContentTypeEnum.create(chatMsg.getContentType().getValue().toUpperCase()));
            assistantMessage.setUserId(currentUserInfo.getId());
            assistantMessage.setCreateBy(currentUserInfo.getUsername());
            assistantMessage.setCreateTime(new Date());
            messageService.save(assistantMessage);
        }

        AssistantMsgView assistantMsgView = new AssistantMsgView();
        assistantMsgView.setId(chatMsg.getId());
        assistantMsgView.setConversationId(chatMsg.getConversationId());
        assistantMsgView.setBotId(chatMsg.getBotId());
        assistantMsgView.setEvent(AssistantMsgEventEnum.MESSAGE_COMPLETED);
        assistantMsgView.setSendFrom(SendFromEnum.ASSISTANT);
        assistantMsgView.setType(AssistantMsgTypeEnum.create(chatMsg.getType().getValue().toUpperCase()));
        assistantMsgView.setContent(chatMsg.getContent());
        assistantMsgView.setContentType(ContentTypeEnum.create(chatMsg.getContentType().getValue().toUpperCase()));
        assistantMsgView.setUserId(currentUserInfo.getId());
        assistantMsgView.setCreateTime(CozeHelper.toDate(chatMsg.getCreatedAt()));
        chatWebSocket.chatWithAssistant(currentUserInfo.getId(), assistantMsgView);
    }

    @Async
    public void streamingChat(GetAssistantResponse assistant, SystemUserVO currentUserInfo, AssistantConversation conversation, MessageView msg) {
        String msgContent = buildMsgContent(msg.getMsgType(), msg.getContent());
        ConversationMsgView msgView = new ConversationMsgView();
        msgView.setRole(RoleEnum.USER);
        msgView.setContent(msgContent);
        msgView.setContentType(toContentType(msg.getMsgType()));
        saveAndSendUserMsg(currentUserInfo, assistant, conversation.getId(), msg, msgView);

        cozeHandler.streamingChat(assistant.getId(), currentUserInfo.getId(), conversation.getConversationId(), msgView, serverSentEvent -> {
            log.info("responseStr={}", serverSentEvent.toString());
            StreamEventEnum event = StreamEventEnum.create(serverSentEvent.event());
            if (StreamEventEnum.MESSAGE_DELTA == event) {
                String jsonString = JSONObject.toJSONString(serverSentEvent.data());
                MsgView messageView = JSONObject.parseObject(jsonString, MsgView.class);
                sendMsg(assistant.getId(), currentUserInfo, conversation.getId(), event, messageView);
            }
            if (StreamEventEnum.MESSAGE_COMPLETED == event) {
                String jsonString = JSONObject.toJSONString(serverSentEvent.data());
                MsgView messageView = JSONObject.parseObject(jsonString, MsgView.class);
                if (MessageTypeEnum.ANSWER == messageView.getType()) {
                    AssistantMessage assistantMessage = new AssistantMessage();
                    assistantMessage.setConversationId(conversation.getId());
                    assistantMessage.setBotId(assistant.getId());
                    assistantMessage.setMessageId(messageView.getId());
                    assistantMessage.setSendFrom(SendFromEnum.ASSISTANT);
                    assistantMessage.setContent(messageView.getContent());
                    assistantMessage.setContentType(ContentTypeEnum.create(messageView.getContentType().getValue().toUpperCase()));
                    assistantMessage.setUserId(currentUserInfo.getId());
                    assistantMessage.setCreateBy(currentUserInfo.getUsername());
                    assistantMessage.setCreateTime(new Date());
                    messageService.save(assistantMessage);
                }
                sendMsg(assistant.getId(), currentUserInfo, conversation.getId(), event, messageView);
            } else if (event.isChatEvent()) {
                String jsonString = JSONObject.toJSONString(serverSentEvent.data());
                ChatView chatView = JSONObject.parseObject(jsonString, ChatView.class);
                log.info(JSONObject.toJSONString(chatView));
                sendChat(assistant.getId(), currentUserInfo, conversation.getId(), event, chatView);
            } else if (event.isDone()) {
                sendDone(assistant, currentUserInfo, conversation.getId(), serverSentEvent.data() + "");
                log.info("chat done, assistantId={}, conversationId={}, userId={}!", assistant.getId(), conversation.getId(), currentUserInfo.getId());
            } else if (event.isError()) {
                log.error("Failed to handle chat, assistantId={}, conversationId={}, userId={}!", assistant.getId(), conversation.getId(), currentUserInfo.getId());
                sendError(assistant, currentUserInfo, conversation.getId());
            }
        });
    }

    private void sendError(GetAssistantResponse assistant, SystemUserVO currentUserInfo, String conversationId) {
        AssistantMsgView assistantMsgView = new AssistantMsgView();
        assistantMsgView.setId(UUID.randomUUID().toString());
        assistantMsgView.setConversationId(conversationId);
        assistantMsgView.setBotId(assistant.getId());
        assistantMsgView.setEvent(AssistantMsgEventEnum.ERROR);
        assistantMsgView.setSendFrom(SendFromEnum.ASSISTANT);
        assistantMsgView.setType(AssistantMsgTypeEnum.ANSWER);
        assistantMsgView.setContent("Failed to handle chat!");
        assistantMsgView.setContentType(ContentTypeEnum.TEXT);
        assistantMsgView.setUserId(currentUserInfo.getId());
        assistantMsgView.setCreateTime(new Date());
        chatWebSocket.chatWithAssistant(currentUserInfo.getId(), assistantMsgView);
    }
    private void sendDone(GetAssistantResponse assistant, SystemUserVO currentUserInfo, String conversationId, String content) {
        AssistantMsgView assistantMsgView = new AssistantMsgView();
        assistantMsgView.setId(UUID.randomUUID().toString());
        assistantMsgView.setConversationId(conversationId);
        assistantMsgView.setBotId(assistant.getId());
        assistantMsgView.setEvent(AssistantMsgEventEnum.DONE);
        assistantMsgView.setSendFrom(SendFromEnum.ASSISTANT);
        assistantMsgView.setType(AssistantMsgTypeEnum.ANSWER);
        assistantMsgView.setContent(content);
        assistantMsgView.setContentType(ContentTypeEnum.TEXT);
        assistantMsgView.setUserId(currentUserInfo.getId());
        assistantMsgView.setCreateTime(new Date());
        chatWebSocket.chatWithAssistant(currentUserInfo.getId(), assistantMsgView);
    }

    private void sendMsg(String assistantId, SystemUserVO currentUserInfo, String conversationId, StreamEventEnum event, MsgView messageView) {
        AssistantMsgView assistantMsgView = new AssistantMsgView();
        assistantMsgView.setId(UUID.randomUUID().toString());
        assistantMsgView.setBotId(assistantId);
        assistantMsgView.setConversationId(conversationId);
        assistantMsgView.setEvent(AssistantMsgEventEnum.create(event.name()));
        assistantMsgView.setSendFrom(SendFromEnum.ASSISTANT);
        assistantMsgView.setType(AssistantMsgTypeEnum.create(messageView.getType().getValue().toUpperCase()));
        assistantMsgView.setContent(messageView.getContent());
        assistantMsgView.setContentType(ContentTypeEnum.create(messageView.getContentType().getValue().toUpperCase()));
        assistantMsgView.setUserId(currentUserInfo.getId());
        assistantMsgView.setCreateTime(CozeHelper.toDate(messageView.getCreatedAt()));
        chatWebSocket.chatWithAssistant(currentUserInfo.getId(), assistantMsgView);
    }

    private void sendChat(String assistantId, SystemUserVO currentUserInfo, String conversationId, StreamEventEnum event, ChatView chatView) {
        AssistantMsgView assistantMsgView = new AssistantMsgView();
        assistantMsgView.setId(UUID.randomUUID().toString());
        assistantMsgView.setBotId(assistantId);
        assistantMsgView.setConversationId(conversationId);
        assistantMsgView.setEvent(AssistantMsgEventEnum.create(event.name()));
        assistantMsgView.setSendFrom(SendFromEnum.ASSISTANT);
        assistantMsgView.setContent(JSONObject.toJSONString(chatView));
        assistantMsgView.setContentType(ContentTypeEnum.OBJECT_STRING);
        assistantMsgView.setUserId(currentUserInfo.getId());
        assistantMsgView.setCreateTime(CozeHelper.toDate(chatView.getCreatedAt()));
        chatWebSocket.chatWithAssistant(currentUserInfo.getId(), assistantMsgView);
    }

    private void saveAndSendUserMsg(SystemUserVO currentUserInfo, GetAssistantResponse assistant, String msgConversation, MessageView msg, ConversationMsgView msgView) {
        AssistantMessage assistantMessage = new AssistantMessage();
        assistantMessage.setConversationId(msgConversation);
        assistantMessage.setBotId(assistant.getId());
        assistantMessage.setMessageId(null);
        assistantMessage.setSendFrom(SendFromEnum.USER);
        assistantMessage.setContent(msg.getContent());
        assistantMessage.setContentType(ContentTypeEnum.create(msgView.getContentType().getValue().toUpperCase()));
        assistantMessage.setUserId(currentUserInfo.getId());
        assistantMessage.setCreateBy(currentUserInfo.getUsername());
        assistantMessage.setCreateTime(new Date());
        messageService.save(assistantMessage);

        AssistantMsgView assistantMsgView = new AssistantMsgView();
        assistantMsgView.setId(assistantMessage.getId());
        assistantMsgView.setConversationId(assistantMessage.getConversationId());
        assistantMsgView.setBotId(assistantMessage.getBotId());
        assistantMsgView.setEvent(AssistantMsgEventEnum.MESSAGE_COMPLETED);
        assistantMsgView.setSendFrom(SendFromEnum.USER);
        assistantMsgView.setType(AssistantMsgTypeEnum.QUERY);
        assistantMsgView.setContent(assistantMessage.getContent());
        assistantMsgView.setContentType(assistantMessage.getContentType());
        assistantMsgView.setUserId(currentUserInfo.getId());
        assistantMsgView.setCreateTime(assistantMessage.getCreateTime());
        chatWebSocket.chatWithAssistant(currentUserInfo.getId(), assistantMsgView);
    }

    private static String buildMsgContent(MsgTypeEnum msgType, String content) {
        String msgContent;
        if (MsgTypeEnum.TEXT.equals(msgType)) {
            msgContent = content;
        } else if (MsgTypeEnum.IMAGE.equals(msgType)) {
            ObjectContent objectContent = new ObjectContent();
            objectContent.setType(ObjectContentTypeEnum.IMAGE);
            objectContent.setFileId(content);
            List<ObjectContent> objectContents = List.of(objectContent);
            msgContent = JSONObject.toJSONString(objectContents);
        } else {
            throw new BadRequestException("Unsupported message type={}!");
        }
        return msgContent;
    }

    private static MsgContentTypeEnum toContentType(MsgTypeEnum msgType) {
        return switch (msgType) {
            case TEXT -> MsgContentTypeEnum.TEXT;
            case IMAGE -> MsgContentTypeEnum.OBJECT_STRING;
        };
    }

    public UploadFileResponse uploadFile(SystemUserVO userVO, MultipartFile multipartFile) {
        CozeFile cozeFile = cozeFileService.getByFileName(multipartFile.getOriginalFilename());
        if (Objects.nonNull(cozeFile)) {
            return buildUploadFileResponse(cozeFile);
        }

        String fileUrl = fileService.upload(multipartFile);
        FileView fileView = cozeHandler.uploadFile(multipartFile);
        log.info("Uploaded file to coze success, fileView={}", JSONObject.toJSONString(fileView));
        CozeFile cozeFileCreated = createCozeFile(userVO, fileView, fileUrl);
        return buildUploadFileResponse(cozeFileCreated);
    }

    @Cacheable(value = "assistant:file:", key = "#fileId")
    public GetFileResponse getFile(String fileId) {
        CozeFile cozeFile = cozeFileService.getById(fileId);
        return Objects.isNull(cozeFile) ? null : buildGetFileResponse(cozeFile);
    }

    @NotNull
    private static UploadFileResponse buildUploadFileResponse(CozeFile cozeFile) {
        UploadFileResponse response = new UploadFileResponse();
        response.setId(cozeFile.getId());
        response.setCozeId(cozeFile.getCozeId());
        response.setCreatedTime(cozeFile.getCreateTime());
        response.setFileName(cozeFile.getFileName());
        response.setFileUrl(cozeFile.getFileUrl());
        return response;
    }

    private CozeFile createCozeFile(SystemUserVO userVO, FileView fileView, String fileUrl) {
        CozeFile cozeFile = new CozeFile();
        cozeFile.setCozeId(fileView.getId());
        cozeFile.setFileName(fileView.getFileName());
        cozeFile.setFileUrl(fileUrl);
        cozeFile.setBytes(fileView.getBytes());
        cozeFile.setUserId(userVO.getId());
        cozeFile.setCreateTime(CozeHelper.toDate(fileView.getCreatedAt()));
        cozeFile.setCreateBy(userVO.getUsername());
        cozeFileService.save(cozeFile);
        return cozeFile;
    }

    @NotNull
    private static ListAssistantResponse buildListAssistantResponse(ListBotResponse listBotResponse) {
        List<AssistantSummaryView> assistantSummaries = listBotResponse.getSpaceBots().stream().map(botSummaryView -> {
            AssistantSummaryView assistant = new AssistantSummaryView();
            assistant.setId(botSummaryView.getBotId());
            assistant.setName(botSummaryView.getBotName());
            assistant.setDescription(botSummaryView.getDescription());
            assistant.setIconUrl(botSummaryView.getIconUrl());
            assistant.setPublishedTime(DateHelper.toDate(botSummaryView.getPublishTime()).orElse(null));
            return assistant;
        }).toList();

        ListAssistantResponse response = new ListAssistantResponse();
        response.setAssistantSummaries(assistantSummaries);
        return response;
    }

    @NotNull
    private static GetAssistantResponse buildGetAssistantResponse(BotDetailView botDetailView) {
        String prompt = Optional.ofNullable(botDetailView.getPromptInfo()).map(PromptInfo::getPrompt).orElse(null);
        AssistantOnboardInfo onboardingInfo = Optional.ofNullable(botDetailView.getOnboardingInfo()).map(o -> {
            AssistantOnboardInfo onboardInfo = new AssistantOnboardInfo();
            onboardInfo.setPrologue(o.getPrologue());
            onboardInfo.setSuggestedQuestions(o.getSuggestedQuestions());
            return onboardInfo;
        }).orElse(null);

        ModelView modelInfo = botDetailView.getModelInfo();
        AssistantModelView modelView = new AssistantModelView();
        modelView.setId(modelInfo.getModelId());
        modelView.setName(modelInfo.getModelName());

        List<AssistantPlugView> plugViews = ListHelper.ofNullable(botDetailView.getPluginInfoList())
                .stream().map(AssistantService::buildAssistantPlugView).toList();

        GetAssistantResponse response = new GetAssistantResponse();
        response.setId(botDetailView.getBotId());
        response.setName(botDetailView.getName());
        response.setDescription(botDetailView.getDescription());
        response.setIconUrl(botDetailView.getIconUrl());
        response.setCreateTime(botDetailView.getCreateTime());
        response.setUpdateTime(botDetailView.getUpdateTime());
        response.setVersion(botDetailView.getVersion());
        response.setPrompt(prompt);
        response.setOnboardingInfo(onboardingInfo);
        response.setBotMode(botDetailView.getBotMode());
        response.setModel(modelView);
        response.setPlugins(plugViews);
        return response;
    }

    @NotNull
    private static AssistantPlugView buildAssistantPlugView(Plugin p) {
        List<AssistantPlugAPIView> assistantPlugAPIViews = ListHelper.ofNullable(p.getApiInfoList()).stream().map(api -> {
            AssistantPlugAPIView assistantPlugAPIView = new AssistantPlugAPIView();
            assistantPlugAPIView.setId(api.getApiId());
            assistantPlugAPIView.setName(api.getName());
            assistantPlugAPIView.setDescription(api.getDescription());
            return assistantPlugAPIView;
        }).toList();

        AssistantPlugView assistantPlugView = new AssistantPlugView();
        assistantPlugView.setId(p.getPluginId());
        assistantPlugView.setName(p.getName());
        assistantPlugView.setDescription(p.getDescription());
        assistantPlugView.setIconUrl(p.getIconUrl());
        assistantPlugView.setApiList(assistantPlugAPIViews);
        return assistantPlugView;
    }

    @NotNull
    private static GetFileResponse buildGetFileResponse(CozeFile cozeFile) {
        GetFileResponse response = new GetFileResponse();
        response.setId(cozeFile.getId());
        response.setCozeId(cozeFile.getCozeId());
        response.setCreatedTime(cozeFile.getCreateTime());
        response.setFileName(cozeFile.getFileName());
        response.setFileUrl(cozeFile.getFileUrl());
        return response;
    }
}
