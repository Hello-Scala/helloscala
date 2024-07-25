package com.helloscala.web.service.bot;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
import com.helloscala.common.vo.message.ImMessageHelper;
import com.helloscala.common.vo.message.ImMessageVO;
import com.helloscala.common.vo.user.SystemUserVO;
import com.helloscala.common.web.exception.BadRequestException;
import com.helloscala.web.controller.coze.request.ChatWithAssistantRequest;
import com.helloscala.web.controller.coze.request.ListConversationRequest;
import com.helloscala.web.controller.coze.request.MessageView;
import com.helloscala.web.controller.coze.response.*;
import com.helloscala.web.im.MessageConstant;
import com.helloscala.web.service.client.coze.CozeHandler;
import com.helloscala.web.service.client.coze.request.*;
import com.helloscala.web.service.client.coze.response.*;
import com.helloscala.web.websocket.ChatWebSocket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    private final ChatWebSocket chatWebSocket;

    @Cacheable(value = "assistant:list")
    public ListAssistantResponse list() {
        ListBotResponse listBotResponse = cozeHandler.listBots();
        return buildListAssistantResponse(listBotResponse);
    }

    @Cacheable(value = "assistant:", key = "#id")
    public GetAssistantResponse get(String id) {
        BotDetailView botDetailView = cozeHandler.getBot(id);
        return buildGetAssistantResponse(botDetailView);
    }

    public ListConversationResponse listConversation(ListConversationRequest request) {
        IPage<AssistantConversation> conversationPage = conversationService.list(request.getId(), request.getPageNo(), request.getPageSize());
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
        response.setConversations(conversationViews);
        return response;
    }

    public ChatWithAssistantResponse chat(GetAssistantResponse assistant, SystemUserVO currentUserInfo, ChatWithAssistantRequest request) {
        String id = assistant.getBotId();
        String msgConversation = request.getConversationId();
        MessageView msg = request.getMsg();
        if (StrUtil.isBlank(request.getConversationId())) {
            // todo create conversation
            String content = msg.getContent();
            String summary = content.length() < 50 ? content : StrUtil.subPre(content, 50) + "...";
            ConversationView conversation = cozeHandler.createConversation();
            AssistantConversation assistantConversation = new AssistantConversation();
            assistantConversation.setBotId(id);
            assistantConversation.setConversationId(conversation.getId());
            assistantConversation.setUserId(currentUserInfo.getId());
            assistantConversation.setSummary(summary);
            assistantConversation.setCreateTime(CozeHelper.toDate(conversation.getCreatedAt()));
            assistantConversation.setCreateBy(currentUserInfo.getUsername());
            conversationService.save(assistantConversation);
            msgConversation = assistantConversation.getId();
        }
        chat(assistant, currentUserInfo, msg, msgConversation, id);
        ChatWithAssistantResponse assistantResponse = new ChatWithAssistantResponse();
        assistantResponse.setConversationId(msgConversation);
        return assistantResponse;
    }

    @Async
    public void chat(GetAssistantResponse assistant, SystemUserVO currentUserInfo, MessageView msg, String msgConversation, String id) {
        String msgContent = buildMsgContent(msg.getMsgType(), msg.getContent());
        ConversationMsgView msgView = new ConversationMsgView();
        msgView.setRole(RoleEnum.USER);
        msgView.setType(MessageTypeEnum.QUERY);
        msgView.setContent(msgContent);
        msgView.setContentType(toContentType(msg.getMsgType()));
        saveAndSendUserMsg(currentUserInfo, msgConversation, id, msg, msgView);

        final String finalMsgConversation = msgConversation;
        cozeHandler.chat(id, currentUserInfo.getId(), msgConversation, msgView, streamResponse -> {
            StreamEventEnum event = streamResponse.getEvent();
            if (event.isMsgEvent()) {
                String jsonString = JSONObject.toJSONString(streamResponse.getData());
                MsgView messageView = JSONObject.parseObject(jsonString, MsgView.class);
                AssistantMessage assistantMessage = new AssistantMessage();
                assistantMessage.setConversationId(finalMsgConversation);
                assistantMessage.setBotId(id);
                assistantMessage.setMessageId(null);
                assistantMessage.setSendFrom(SendFromEnum.ASSISTANT);
                assistantMessage.setContent(messageView.getContent());
                assistantMessage.setContentType(ContentTypeEnum.create(messageView.getContentType().getValue().toUpperCase()));
                assistantMessage.setUserId(currentUserInfo.getId());
                assistantMessage.setCreateBy(currentUserInfo.getUsername());
                assistantMessage.setCreateTime(new Date());
                messageService.save(assistantMessage);

                ImMessageVO imMessageVO = new ImMessageVO();
                imMessageVO.setCode(MessageConstant.PRIVATE_CHAT_CODE);
                imMessageVO.setFromUserId(assistant.getBotId());
                imMessageVO.setFromUserNickname(assistant.getName());
                imMessageVO.setFromUserAvatar(assistant.getIconUrl());
                imMessageVO.setContent(assistantMessage.getContent());
                imMessageVO.setType(ImMessageHelper.toType(assistantMessage.getContentType()));
                chatWebSocket.chat(imMessageVO);
            } else if (event.isChatEvent()) {
                String jsonString = JSONObject.toJSONString(streamResponse.getData());
                ChatView chatView = JSONObject.parseObject(jsonString, ChatView.class);
                log.info(JSONObject.toJSONString(chatView));
            } else if (event.isDone()) {
                log.info("chat done, assistantId={}, conversationId={}, userId={}!", id, finalMsgConversation, currentUserInfo.getId());
            } else if (event.isError()) {
                log.error("Failed to handle chat, assistantId={}, conversationId={}, userId={}!", id, finalMsgConversation, currentUserInfo.getId());
                ImMessageVO imMessageVO = new ImMessageVO();
                imMessageVO.setCode(MessageConstant.PRIVATE_CHAT_CODE);
                imMessageVO.setFromUserId(assistant.getBotId());
                imMessageVO.setFromUserNickname(assistant.getName());
                imMessageVO.setFromUserAvatar(assistant.getIconUrl());
                imMessageVO.setContent("Failed to handle chat!");
                imMessageVO.setType(1);
                chatWebSocket.chat(imMessageVO);
            }
        });
    }

    private void saveAndSendUserMsg(SystemUserVO currentUserInfo, String msgConversation, String id, MessageView msg, ConversationMsgView msgView) {
        AssistantMessage assistantMessage = new AssistantMessage();
        assistantMessage.setConversationId(msgConversation);
        assistantMessage.setBotId(id);
        assistantMessage.setMessageId(null);
        assistantMessage.setSendFrom(SendFromEnum.USER);
        assistantMessage.setContent(msg.getContent());
        assistantMessage.setContentType(ContentTypeEnum.create(msgView.getContentType().getValue().toUpperCase()));
        assistantMessage.setUserId(currentUserInfo.getId());
        assistantMessage.setCreateBy(currentUserInfo.getUsername());
        assistantMessage.setCreateTime(new Date());
        messageService.save(assistantMessage);

        ImMessageVO imMessageVO = new ImMessageVO();
        imMessageVO.setCode(MessageConstant.PRIVATE_CHAT_CODE);
        imMessageVO.setFromUserId(currentUserInfo.getId());
        imMessageVO.setFromUserNickname(currentUserInfo.getNickname());
        imMessageVO.setFromUserAvatar(currentUserInfo.getAvatar());
        imMessageVO.setContent(assistantMessage.getContent());
        imMessageVO.setType(ImMessageHelper.toType(assistantMessage.getContentType()));
        chatWebSocket.chat(imMessageVO);
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
        response.setBotId(botDetailView.getBotId());
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
