package com.helloscala.web.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.helloscala.common.annotation.BusinessLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.vo.message.ImMessageVO;
import com.helloscala.web.service.ApiImMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


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
    public ResponseResult selectHistoryList() {
        return  imMessageService.selectHistoryList();
    }

    @SaCheckLogin
    @GetMapping(value = "/getRoomList")
    @Operation(summary = "Get room list", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get room list")
    public ResponseResult selectRoomList() {
        return imMessageService.selectRoomList();
    }

    @SaCheckLogin
    @GetMapping(value = "/selectUserImHistory")
    @BusinessLogger(value = "Home page list user chat history",type = "search",desc = "Home page list user chat history")
    @Operation(summary = "Home page list user chat history", method = "GET")
    @ApiResponse(responseCode = "200", description = "Home page list user chat history")
    public ResponseResult selectUserImHistoryList(@RequestParam(name = "fromUserId", required = true) String fromUserId,
                                                  @RequestParam(name = "toUserId", required = true) String toUserId) {
        return  imMessageService.selectUserImHistoryList(fromUserId,toUserId);
    }

    @SaCheckLogin
    @PostMapping(value = "/chat")
    @Operation(summary = "Send message", method = "POST")
    @ApiResponse(responseCode = "200", description = "Send message")
    @BusinessLogger(value = "Send message",type = "add",desc = "Send message")
    public ResponseResult chat(@RequestBody ImMessageVO message){
        return imMessageService.chat(message);
    }

    @SaCheckLogin
    @PostMapping(value = "/withdraw")
    @Operation(summary = "Recall message", method = "POST")
    @ApiResponse(responseCode = "200", description = "Recall message")
    @BusinessLogger(value = "Recall message",type = "delete",desc = "Recall message")
    public ResponseResult withdraw(@RequestBody ImMessageVO message){
        return imMessageService.withdraw(message);
    }

    @SaCheckLogin
    @GetMapping(value = "/read")
    @Operation(summary = "Mark as read", method = "GET")
    @ApiResponse(responseCode = "200", description = "Mark as read")
    @BusinessLogger(value = "Mark as read",type = "update",desc = "Mark as read")
    public ResponseResult read(@RequestParam(name = "userId", required = true) String userId) {
        return imMessageService.read(userId);
    }

    @SaCheckLogin
    @DeleteMapping(value = "/deleteRoom")
    @Operation(summary = "Delete room", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete room")
    @BusinessLogger(value = "Delete room",type = "update",desc = "Delete room")
    public ResponseResult deleteRoom(@RequestParam(name = "roomId", required = true) String roomId) {
        return imMessageService.deleteRoom(roomId);
    }

    @SaCheckLogin
    @PostMapping(value = "/addRoom")
    @Operation(summary = "Create room", method = "POST")
    @ApiResponse(responseCode = "200", description = "Create room")
    @BusinessLogger(value = "Create room",type = "update",desc = "Create room")
    public ResponseResult addRoom(@RequestParam(name = "userId", required = true) String userId) {
        return imMessageService.addRoom(userId);
    }

    @SaCheckLogin
    @GetMapping(value = "/getMessageNotice")
    @Operation(summary = "Get system notice", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get system notice")
    public ResponseResult getMessageNotice(@RequestParam(name = "type", required = true) Integer type) {
        return imMessageService.getMessageNotice(type);
    }

    @SaCheckLogin
    @GetMapping("/getNewSystemNotice")
    @Operation(summary = "Get latest unread system notice", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get latest unread system notice")
    public ResponseResult getNewSystemNotice(){
        return imMessageService.getNewSystemNotice();
    }

    @SaCheckLogin
    @DeleteMapping("/deleteMessage")
    @Operation(summary = "Delete messages by type", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete messages by type")
    @BusinessLogger(value = "Delete messages by type",type = "delete",desc = "Delete messages by type")
    public ResponseResult deleteMessage(String id,Integer type){
        return imMessageService.deleteByNoticeType(id,type);
    }


    @SaCheckLogin
    @GetMapping(value = "/getMessageNoticeApplet")
    @Operation(summary = "Applet get system notice", method = "GET")
    @ApiResponse(responseCode = "200", description = "Applet get system notice")
    public ResponseResult getMessageNoticeApplet(@RequestParam(name = "type", required = false) Integer type) {
        return imMessageService.getMessageNoticeApplet(type);
    }

    @SaCheckLogin
    @GetMapping(value = "/markReadMessageNoticeApplet")
    @Operation(summary = "Applet mark as read", method = "GET")
    @ApiResponse(responseCode = "200", description = "Applet mark as read")
    @BusinessLogger(value = "Applet mark as read",type = "delete",desc = "Applet mark as read")
    public ResponseResult markReadMessageNoticeApplet(@RequestParam(name = "id", required = true) String id) {
        return imMessageService.markReadMessageNoticeApplet(id);
    }
}
