package com.helloscala.web.controller;

import com.helloscala.common.RedisConstants;
import com.helloscala.common.service.RedisService;
import com.helloscala.web.service.ApiUserService;
import com.helloscala.web.utils.RandomUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Tag(name = "Wechat integration API")
@RestController()
@RequestMapping("/wechat")
@RequiredArgsConstructor
public class ApiWeChatController {

    private final RedisService redisService;

    private final WxMpService wxMpService;

    private final ApiUserService userService;
    private final Pattern pattern = Pattern.compile("(?i)^DL\\d{4}$");

    @Operation(summary = "check token", method = "get")
    @GetMapping(produces = "text/plain;charset=utf-8")
    public String checkSignature(@RequestParam(name = "signature", required = true) String signature,
                                 @RequestParam(name = "timestamp", required = true) String timestamp,
                                 @RequestParam(name = "nonce", required = true) String nonce,
                                 @RequestParam(name = "echostr", required = true) String echostr) {
        if (wxMpService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }
        return "Invalid signature";
    }

    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String handleMsg(HttpServletRequest request) {

        try {
            WxMpXmlMessage message = WxMpXmlMessage.fromXml(request.getInputStream());
            String content = message.getContent();
            log.info("Official account request type:{};content:{}", message.getMsgType(), content);
            if (WxConsts.XmlMsgType.TEXT.equals(message.getMsgType())){
                if ("验证码".equals(content)) {
                    String code = RandomUtil.generationNumberChar(6);
                    String msg = MessageFormat.format("您的本次验证码:{0},该验证码30分钟内有效。", code);
                    redisService.setCacheObject(RedisConstants.WECHAT_CODE+code,code,30, TimeUnit.MINUTES);
                    return returnMsg(msg, message);
                }
                //登录逻辑
                Matcher matcher = pattern.matcher(content);
                if (!matcher.matches()) {
                    return returnMsg("验证不正确或已过期", message);
                }else {
                    String msg = userService.wechatLogin(message);
                    return returnMsg(msg, message);
                }
   
            }

        } catch (Exception e) {
            log.error("Failed to handle message!", e);
        }
        return "";
    }

    private static String returnMsg(String msg, WxMpXmlMessage message) {
        WxMpXmlOutTextMessage outMessage = WxMpXmlOutTextMessage.TEXT().content(msg).fromUser(message.getToUser()).toUser(message.getFromUser()).build();
        return outMessage.toXml();
    }
}

