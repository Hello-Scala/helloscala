package com.helloscala.web.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.annotation.BusinessLogger;
import com.helloscala.common.vo.message.ImMessageVO;
import com.helloscala.common.vo.message.ImRoomListVO;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import com.helloscala.web.service.ApiImMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/v1/im")
@Tag(name = "Message management")
@RequiredArgsConstructor
public class ApiImMessageController {

    private final ApiImMessageService imMessageService;


    @SaCheckLogin
    @GetMapping(value = "/")
    @BusinessLogger(value = "home page list chat history",type = "search",desc = "home page list chat history")
    @Operation(summary = "Home page list chat history", method = "GET")
    @ApiResponse(responseCode = "200", description = "Home page list chat history")
    public Response<Page<ImMessageVO>> selectHistoryList() {
        Page<ImMessageVO> imMessageVOPage = imMessageService.selectHistoryList();
        return ResponseHelper.ok(imMessageVOPage);
    }

    @SaCheckLogin
    @GetMapping(value = "/getRoomList")
    @Operation(summary = "Get room list", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get room list")
    public Response<List<ImRoomListVO>> selectRoomList() {
        List<ImRoomListVO> imRoomListVOS = imMessageService.selectRoomList();
        return ResponseHelper.ok(imRoomListVOS);
    }

    @SaCheckLogin
    @GetMapping(value = "/selectUserImHistory")
    @BusinessLogger(value = "Home page list user chat history",type = "search",desc = "Home page list user chat history")
    @Operation(summary = "Home page list user chat history", method = "GET")
    @ApiResponse(responseCode = "200", description = "Home page list user chat history")
    public Response<Page<ImMessageVO>> selectUserImHistoryList(@RequestParam(name = "fromUserId", required = true) String fromUserId,
                                                               @RequestParam(name = "toUserId", required = true) String toUserId) {
        Page<ImMessageVO> imMessageVOPage = imMessageService.selectUserImHistoryList(fromUserId, toUserId);
        return ResponseHelper.ok(imMessageVOPage);
    }

    @SaCheckLogin
    @PostMapping(value = "/chat")
    @Operation(summary = "Send message", method = "POST")
    @ApiResponse(responseCode = "200", description = "Send message")
    @BusinessLogger(value = "Send message",type = "add",desc = "Send message")
    public EmptyResponse chat(@RequestBody ImMessageVO message){
        imMessageService.chat(message);
        return ResponseHelper.ok();
    }

    @SaCheckLogin
    @PostMapping(value = "/withdraw")
    @Operation(summary = "Recall message", method = "POST")
    @ApiResponse(responseCode = "200", description = "Recall message")
    @BusinessLogger(value = "Recall message",type = "delete",desc = "Recall message")
    public EmptyResponse withdraw(@RequestBody ImMessageVO message){
        imMessageService.withdraw(message);
        return ResponseHelper.ok();
    }

    @SaCheckLogin
    @GetMapping(value = "/read")
    @Operation(summary = "Mark as read", method = "GET")
    @ApiResponse(responseCode = "200", description = "Mark as read")
    @BusinessLogger(value = "Mark as read",type = "update",desc = "Mark as read")
    public EmptyResponse read(@RequestParam(name = "userId", required = true) String userId) {
        imMessageService.read(userId);
        return ResponseHelper.ok();
    }

    @SaCheckLogin
    @DeleteMapping(value = "/deleteRoom")
    @Operation(summary = "Delete room", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete room")
    @BusinessLogger(value = "Delete room",type = "update",desc = "Delete room")
    public EmptyResponse deleteRoom(@RequestParam(name = "roomId", required = true) String roomId) {
        imMessageService.deleteRoom(roomId);
        return ResponseHelper.ok();
    }

    @SaCheckLogin
    @PostMapping(value = "/addRoom")
    @Operation(summary = "Create room", method = "POST")
    @ApiResponse(responseCode = "200", description = "Create room")
    @BusinessLogger(value = "Create room",type = "update",desc = "Create room")
    public Response<ImRoomListVO> addRoom(@RequestParam(name = "userId", required = true) String userId) {
        ImRoomListVO imRoomListVO = imMessageService.addRoom(userId);
        return ResponseHelper.ok(imRoomListVO);
    }

    @SaCheckLogin
    @GetMapping(value = "/getMessageNotice")
    @Operation(summary = "Get system notice", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get system notice")
    public Response<Page<ImMessageVO>> getMessageNotice(@RequestParam(name = "type", required = true) Integer type) {
        Page<ImMessageVO> messageNotice = imMessageService.getMessageNotice(type);
        return ResponseHelper.ok(messageNotice);
    }

    // todo refactor
    @SaCheckLogin
    @GetMapping("/getNewSystemNotice")
    @Operation(summary = "Get latest unread system notice", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get latest unread system notice")
    public Response<Map<String, Long>> getNewSystemNotice(){
        Map<String, Long> newSystemNotice = imMessageService.getNewSystemNotice();
        return ResponseHelper.ok(newSystemNotice);
    }

    @SaCheckLogin
    @DeleteMapping("/deleteMessage")
    @Operation(summary = "Delete messages by type", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete messages by type")
    @BusinessLogger(value = "Delete messages by type",type = "delete",desc = "Delete messages by type")
    public EmptyResponse deleteMessage(String id, Integer type){
        imMessageService.deleteByNoticeType(id,type);
        return ResponseHelper.ok();
    }


    @SaCheckLogin
    @GetMapping(value = "/getMessageNoticeApplet")
    @Operation(summary = "Applet get system notice", method = "GET")
    @ApiResponse(responseCode = "200", description = "Applet get system notice")
    public Response<Page<ImMessageVO>> getMessageNoticeApplet(@RequestParam(name = "type", required = false) Integer type) {
        Page<ImMessageVO> messageNoticeApplet = imMessageService.getMessageNoticeApplet(type);
        return ResponseHelper.ok(messageNoticeApplet);
    }

    @SaCheckLogin
    @GetMapping(value = "/markReadMessageNoticeApplet")
    @Operation(summary = "Applet mark as read", method = "GET")
    @ApiResponse(responseCode = "200", description = "Applet mark as read")
    @BusinessLogger(value = "Applet mark as read",type = "delete",desc = "Applet mark as read")
    public EmptyResponse markReadMessageNoticeApplet(@RequestParam(name = "id", required = true) String id) {
        imMessageService.markReadMessageNoticeApplet(id);
        return ResponseHelper.ok();
    }
}
