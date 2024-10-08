package com.helloscala.web.controller.coze;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.http.ContentType;
import com.helloscala.common.service.UserService;
import com.helloscala.common.vo.user.SystemUserVO;
import com.helloscala.common.web.exception.BadRequestException;
import com.helloscala.common.web.exception.ForbiddenException;
import com.helloscala.common.web.exception.NotFoundException;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import com.helloscala.web.controller.coze.request.ChatWithAssistantRequest;
import com.helloscala.web.controller.coze.response.*;
import com.helloscala.web.service.bot.AssistantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * @author Steve Zou
 */
@Slf4j
@RestController
@RequestMapping("/bot-assistant")
@Tag(name = "Bot Assistant")
@RequiredArgsConstructor
public class BotAssistantController {
    private final AssistantService assistantService;
    private final UserService userService;

    @GetMapping
    @Operation(summary = "list assistant", method = "GET")
    @ApiResponse(responseCode = "200", description = "Assistant list")
    public Response<ListAssistantResponse> list() {
        ListAssistantResponse response = assistantService.list();
        return ResponseHelper.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "get assistant", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get assistant")
    public Response<GetAssistantResponse> get(@PathVariable("id") String id) {
        GetAssistantResponse response = assistantService.get(id);
        return ResponseHelper.ok(response);
    }

    @GetMapping("/{id}/conversation")
    @Operation(summary = "List conversation", method = "GET")
    @ApiResponse(responseCode = "200", description = "List conversation")
    public Response<ListConversationResponse> listConversation(@PathVariable("id") String id,
                                                               @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                               @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        ListConversationResponse response = assistantService.listConversation(id, pageNo, pageSize);
        return ResponseHelper.ok(response);
    }

    @GetMapping("/conversation/{conversationId}/msg")
    @Operation(summary = "List conversation messages", method = "GET")
    @ApiResponse(responseCode = "200", description = "List conversation messages")
    public Response<ListConversationMsgResponse> listConversationMsg(@PathVariable("conversationId") String conversationId,
                                                                     @RequestParam(name = "createTimeEnd", required = false) String createTimeEnd,
                                                                     @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                                     @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        ListConversationMsgResponse response = assistantService.listConversationMsg(conversationId, createTimeEnd, pageNo, pageSize);
        return ResponseHelper.ok(response);
    }

    @PostMapping("/{id}/chat")
    @Operation(summary = "Chat with assistant", method = "Post")
    @ApiResponse(responseCode = "200", description = "Chat with assistant")
    public Response<ChatWithAssistantResponse> chat(@PathVariable(value = "id") String id, @RequestBody ChatWithAssistantRequest request) {
        SystemUserVO currentUserInfo = userService.getCurrentUserInfo();
        log.info("User {} chat with assistant={}, conversationId={}", currentUserInfo.getId(), id, request.getConversationId());
        if (Objects.isNull(request.getMsg())) {
            throw new BadRequestException("Message required!");
        }
        GetAssistantResponse assistant = assistantService.get(id);
        if (Objects.isNull(assistant)) {
            throw new NotFoundException("Assistant not found, id={}!", id);
        }
        ChatWithAssistantResponse response = assistantService.streamingChat(assistant, currentUserInfo, request);
        return ResponseHelper.ok(response);
    }

    @PostMapping("/file")
    @Operation(summary = "Upload file", method = "POST")
    @ApiResponse(responseCode = "201", description = "Upload file")
    public Response<UploadFileResponse> uploadFile(@RequestPart("multipartFile") MultipartFile multipartFile) {
        String loginId = StpUtil.getLoginIdAsString();
        if (Objects.isNull(loginId)) {
            throw new ForbiddenException("Login required!");
        }
        SystemUserVO userVO = userService.get(loginId);
        if (Objects.isNull(userVO)) {
            throw new NotFoundException("User not found, id={}!", loginId);
        }
        log.info("Upload file for assistant, userId={}, fileName={}", userVO.getId(), multipartFile.getName());
        UploadFileResponse response = assistantService.uploadFile(userVO, multipartFile);
        return ResponseHelper.ok(HttpStatus.CREATED, response);
    }

    @PostMapping("/file/{fileId}")
    @Operation(summary = "Get file", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get file")
    public Response<GetFileResponse> getFile(@PathVariable("fileId") String fileId) {
        GetFileResponse fileResponse = assistantService.getFile(fileId);
        return ResponseHelper.ok(fileResponse);
    }
}
