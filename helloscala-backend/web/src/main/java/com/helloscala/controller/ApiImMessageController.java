package com.helloscala.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.helloscala.annotation.BusinessLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.service.ApiImMessageService;
import com.helloscala.vo.message.ImMessageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/im")
@Tag(name = "聊天管理-门户")
@RequiredArgsConstructor
public class ApiImMessageController {

    private final ApiImMessageService imMessageService;


    @SaCheckLogin
    @GetMapping(value = "/")
    @BusinessLogger(value = "首页-获取历史聊天记录",type = "查询",desc = "获取历史聊天记录")
    @Operation(summary = "获取历史聊天记录", method = "GET")
    @ApiResponse(responseCode = "200", description = "获取历史聊天记录")
    public ResponseResult selectHistoryList() {
        return  imMessageService.selectHistoryList();
    }

    @SaCheckLogin
    @GetMapping(value = "/getRoomList")
    @Operation(summary = "获取房间列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "获取房间列表")
    public ResponseResult selectRoomList() {
        return imMessageService.selectRoomList();
    }

    @SaCheckLogin
    @GetMapping(value = "/selectUserImHistory")
    @BusinessLogger(value = "首页-获取单聊历史消息",type = "查询",desc = "获取单聊历史消息")
    @Operation(summary = "获取单聊历史消息", method = "GET")
    @ApiResponse(responseCode = "200", description = "获取单聊历史消息")
    public ResponseResult selectUserImHistoryList(@RequestParam(name = "fromUserId", required = true) String fromUserId,
                                                  @RequestParam(name = "toUserId", required = true) String toUserId) {
        return  imMessageService.selectUserImHistoryList(fromUserId,toUserId);
    }

    @SaCheckLogin
    @PostMapping(value = "/chat")
    @Operation(summary = "发送消息", method = "POST")
    @ApiResponse(responseCode = "200", description = "发送消息")
    public ResponseResult chat(@RequestBody ImMessageVO message){
        return imMessageService.chat(message);
    }

    @SaCheckLogin
    @PostMapping(value = "/withdraw")
    @Operation(summary = "撤回消息", method = "POST")
    @ApiResponse(responseCode = "200", description = "撤回消息")
    public ResponseResult withdraw(@RequestBody ImMessageVO message){
        return imMessageService.withdraw(message);
    }

    @SaCheckLogin
    @GetMapping(value = "/read")
    @Operation(summary = "已读消息", method = "GET")
    @ApiResponse(responseCode = "200", description = "已读消息")
    public ResponseResult read(@RequestParam(name = "userId", required = true) String userId) {
        return imMessageService.read(userId);
    }

    @SaCheckLogin
    @DeleteMapping(value = "/deleteRoom")
    @Operation(summary = "删除房间", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "删除房间")
    public ResponseResult deleteRoom(@RequestParam(name = "roomId", required = true) String roomId) {
        return imMessageService.deleteRoom(roomId);
    }

    @SaCheckLogin
    @PostMapping(value = "/addRoom")
    @Operation(summary = "创建房间（即点击私信按钮）", method = "POST")
    @ApiResponse(responseCode = "200", description = "创建房间（即点击私信按钮）")
    public ResponseResult addRoom(@RequestParam(name = "userId", required = true) String userId) {
        return imMessageService.addRoom(userId);
    }

    @SaCheckLogin
    @GetMapping(value = "/getMessageNotice")
    @Operation(summary = "获取系统通知", method = "GET")
    @ApiResponse(responseCode = "200", description = "获取系统通知")
    public ResponseResult getMessageNotice(@RequestParam(name = "type", required = true) Integer type) {
        return imMessageService.getMessageNotice(type);
    }

    @SaCheckLogin
    @GetMapping("/getNewSystemNotice")
    @Operation(summary = "获取未读的最新通知", method = "GET")
    @ApiResponse(responseCode = "200", description = "获取未读的最新通知")
    public ResponseResult getNewSystemNotice(){
        return imMessageService.getNewSystemNotice();
    }

    @SaCheckLogin
    @DeleteMapping("/deleteMessage")
    @Operation(summary = "根据类型删除所有消息", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "根据类型删除所有消息")
    public ResponseResult deleteMessage(String id,Integer type){
        return imMessageService.deleteByNoticeType(id,type);
    }


    @SaCheckLogin
    @GetMapping(value = "/getMessageNoticeApplet")
    @Operation(summary = "获取系统通知-小程序", method = "GET")
    @ApiResponse(responseCode = "200", description = "获取系统通知-小程序")
    public ResponseResult getMessageNoticeApplet(@RequestParam(name = "type", required = false) Integer type) {
        return imMessageService.getMessageNoticeApplet(type);
    }

    @SaCheckLogin
    @GetMapping(value = "/markReadMessageNoticeApplet")
    @Operation(summary = "标记已读-小程序", method = "GET")
    @ApiResponse(responseCode = "200", description = "标记已读-小程序")
    public ResponseResult markReadMessageNoticeApplet(@RequestParam(name = "id", required = true) String id) {
        return imMessageService.markReadMessageNoticeApplet(id);
    }

}
