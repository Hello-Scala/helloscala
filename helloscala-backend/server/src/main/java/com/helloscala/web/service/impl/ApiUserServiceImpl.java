package com.helloscala.web.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helloscala.common.Constants;
import com.helloscala.common.RedisConstants;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.ResultCode;
import com.helloscala.common.config.FtpConfig;
import com.helloscala.common.dto.user.EmailForgetPasswordDTO;
import com.helloscala.common.dto.user.EmailLoginDTO;
import com.helloscala.common.dto.user.EmailRegisterDTO;
import com.helloscala.common.dto.user.UserInfoDTO;
import com.helloscala.common.entity.Article;
import com.helloscala.common.entity.Collect;
import com.helloscala.common.entity.Followed;
import com.helloscala.common.entity.User;
import com.helloscala.common.enums.LoginTypeEnum;
import com.helloscala.common.enums.UserStatusEnum;
import com.helloscala.common.exception.BusinessException;
import com.helloscala.common.mapper.ArticleMapper;
import com.helloscala.common.mapper.CollectMapper;
import com.helloscala.common.mapper.FollowedMapper;
import com.helloscala.common.mapper.UserMapper;
import com.helloscala.common.service.EmailService;
import com.helloscala.common.service.RedisService;
import com.helloscala.common.utils.AesEncryptUtil;
import com.helloscala.common.utils.BeanCopyUtil;
import com.helloscala.common.utils.DateUtil;
import com.helloscala.common.utils.IpUtil;
import com.helloscala.common.vo.user.SystemUserVO;
import com.helloscala.common.vo.user.UserInfoVO;
import com.helloscala.web.dto.WechatAppletDTO;
import com.helloscala.web.service.ApiUserService;
import com.helloscala.web.utils.RandomUtil;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.zhyd.oauth.model.AuthResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.helloscala.common.ResultCode.DISABLE_ACCOUNT;
import static com.helloscala.common.ResultCode.ERROR_PASSWORD;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiUserServiceImpl implements ApiUserService {
    private final static String[] userAvatarList = {"/asserts/20240505/buxie.png","/asserts/20240505/daizhi.png",
            "/asserts/20240505/fennu.png","/asserts/20240505/jingxi.png","/asserts/20240505/kaixin.png",
            "/asserts/20240505/shuashuai.png"};
    private final FtpConfig ftpConfig;
    private final AesEncryptUtil aesEncryptUtil;
    private final UserMapper userMapper;
    private final ArticleMapper articleMapper;
    private final CollectMapper collectMapper;
    private final RedisService redisService;
    private final FollowedMapper followedMapper;
    private final EmailService emailService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String imagePrefix;


    @PostConstruct
    public void init() {
        if (Objects.isNull(imagePrefix)) {
            imagePrefix = ftpConfig.getHttpPath() + "/helloscala";
        }
    }

    @Override
    public ResponseResult emailLogin(EmailLoginDTO vo) {
        User user = userMapper.selectNameAndPassword(vo.getEmail(), aesEncryptUtil.aesEncrypt(vo.getPassword()));
        if (user == null) {
            throw new BusinessException(ERROR_PASSWORD.desc);
        }
        if (user.getStatus() == UserStatusEnum.DISABLED.code) {
            throw new BusinessException(DISABLE_ACCOUNT.desc);
        }

        StpUtil.login(user.getId(), new SaLoginModel().setDevice("PC").setTimeout(60 * 60 * 24 * 7));
        String token = StpUtil.getTokenValueByLoginId(user.getId());
        SystemUserVO userVO = userMapper.getById(user.getId());
        StpUtil.getSession().set(Constants.CURRENT_USER, userVO);

        UserInfoVO userInfoVO = BeanCopyUtil.copyObject(user, UserInfoVO.class);
        userInfoVO.setToken(token);
        return ResponseResult.success(userInfoVO);
    }

    @Override
    public ResponseResult wxIsLogin(String loginCode) {
        Object value =redisService.getCacheObject(RedisConstants.WX_LOGIN_USER + loginCode);
        if (value == null) {
            return ResponseResult.error("user unauthorized");
        }
        UserInfoVO user = objectMapper.convertValue(value, UserInfoVO.class);
        StpUtil.login(user.getId(), new SaLoginModel().setDevice("PC").setTimeout(60 * 60 * 24 * 7));
        user.setToken(StpUtil.getTokenValueByLoginId(user.getId()));
        return ResponseResult.success(user);
    }

    @Override
    public String wechatLogin(WxMpXmlMessage message) {
        String content = message.getContent().toUpperCase();
        boolean loginFlag = redisService.hasKey(RedisConstants.WX_LOGIN_USER_STATUE + content);
        if (!loginFlag) {
            return "验证码已过期，请重试！";
        }
        UserInfoVO userInfoVO = wechatLogin(message.getFromUser());

        redisService.setCacheObject(RedisConstants.WX_LOGIN_USER + content,userInfoVO, 60, TimeUnit.SECONDS);
        return "登录成功，若长时间无响应请刷新页面！";
    }

    @Override
    public ResponseResult getWechatLoginCode() {
        String code = "DL" + RandomUtil.generationNumberChar(4);
        redisService.setCacheObject(RedisConstants.WX_LOGIN_USER_STATUE + code, false, 60, TimeUnit.SECONDS);
        return ResponseResult.success(code);
    }

    @Override
    public ResponseResult selectUserInfo(String userId) {
        userId = StringUtils.isNotBlank(userId) ? userId : StpUtil.getLoginIdAsString();
        UserInfoVO userInfo = userMapper.selectInfoByUserId(userId);
        return ResponseResult.success(userInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateUser(UserInfoDTO vo) {
        User user = userMapper.selectById(StpUtil.getLoginIdAsString());
        if (ObjectUtils.isEmpty(user)) {
            throw  new BusinessException("User not found!");
        }
        user = BeanCopyUtil.copyObject(vo, User.class);
        user.setId(StpUtil.getLoginIdAsString());
        userMapper.updateById(user);

        return ResponseResult.success();
    }

    @Override
    public ResponseResult selectUserInfoByToken(String token) {
        Object userId = StpUtil.getLoginIdByToken(token);
        UserInfoVO userInfoVO = userMapper.selectInfoByUserId(userId);
        return ResponseResult.success(userInfoVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void authLogin(AuthResponse response, String source,  HttpServletResponse httpServletResponse) throws IOException {
        if (response.getData() == null) {
            log.info("User canceled login via source={}",source);
            httpServletResponse.sendRedirect("https://blog.helloscala.com");
            return;
        }
        String result = JSONUtil.toJsonStr(response.getData());
        log.info("login verify result from source={}, result:{}", source, result);

        @SuppressWarnings("unchecked")
        Map<String, Object> paramMap = (Map<String, Object>) JSONUtil.toBean(result, Map.class);
        Object uuid = paramMap.get("uuid");
        String ipAddress = IpUtil.getIp();
        String ipSource = IpUtil.getIp2region(ipAddress);
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, uuid));
        if (Objects.nonNull(user)) {
            userMapper.update(new User(), new LambdaUpdateWrapper<User>()
                    .set(User::getLastLoginTime, LocalDateTime.now())
                    .set(User::getIpAddress, ipAddress)
                    .set(User::getIpSource, ipSource)
                    .eq(User::getId, user.getId()));
        } else {
            user = User.builder()
                    .username(uuid.toString())
                    .password(UUID.randomUUID().toString())
                    .loginType(LoginTypeEnum.getType(source))
                    .lastLoginTime(DateUtil.getNowDate())
                    .ipAddress(ipAddress)
                    .ipSource(ipSource)
                    .roleId(2)
                    .status(UserStatusEnum.NORMAL.getCode())
                    .nickname(source.equals("github") ? paramMap.get("username").toString() : paramMap.get("nickname").toString())
                    .avatar(paramMap.get("avatar").toString())
                    .build();
            userMapper.insert(user);
        }
        StpUtil.login(user.getId(), new SaLoginModel().setDevice("PC").setTimeout(60 * 60 * 24 * 7));
        httpServletResponse.sendRedirect("https://blog.helloscala.com/?token=" + StpUtil.getTokenValueByLoginId(user.getId()));
    }

    @Override
    public ResponseResult sendEmailCode(String email) {
        try {
            emailService.sendCode(email);
            return ResponseResult.success();
        } catch (MessagingException e) {
            throw new BusinessException("Email send failed!");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult emailRegister(EmailRegisterDTO emailRegisterDTO) {
        boolean b = redisService.hasKey(RedisConstants.EMAIL_CODE + emailRegisterDTO.getEmail());
        if (!b) {
            throw new BusinessException(ResultCode.ERROR_EXCEPTION_MOBILE_CODE);
        }
        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getUsername, emailRegisterDTO.getEmail()));
        if (count > 0) {
            throw new BusinessException("Email registered, email={}!", emailRegisterDTO.getEmail());
        }
        User user = User.builder()
                .username(emailRegisterDTO.getEmail())
                .password(aesEncryptUtil.aesEncrypt(emailRegisterDTO.getPassword()))
                .loginType(LoginTypeEnum.getType("email"))
                .roleId(2)
                .status(UserStatusEnum.NORMAL.getCode())
                .nickname(emailRegisterDTO.getNickname())
                .build();
        userMapper.insert(user);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult forgetPassword(EmailForgetPasswordDTO emailForgetPasswordDTO) {
        boolean b = redisService.hasKey(RedisConstants.EMAIL_CODE + emailForgetPasswordDTO.getEmail());
        if (!b) {
            throw new BusinessException(ResultCode.ERROR_EXCEPTION_MOBILE_CODE);
        }
        User user = User.builder().password(aesEncryptUtil.aesEncrypt(emailForgetPasswordDTO.getPassword())).build();
        userMapper.update(user,new LambdaQueryWrapper<User>().eq(User::getUsername,emailForgetPasswordDTO.getEmail()));
        return ResponseResult.success();
    }

    @Override
    public ResponseResult getUserCount(String id) {
        id = StringUtils.isBlank(id) ? StpUtil.getLoginIdAsString() : id;
        Long articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>().eq(Article::getUserId, id));
        Long collectCount = collectMapper.selectCount(new LambdaQueryWrapper<Collect>().eq(Collect::getUserId, id));
        Long followedCount = followedMapper.selectCount(new LambdaQueryWrapper<Followed>().eq(Followed::getUserId, id));
        return ResponseResult.success().putExtra("articleCount", articleCount).putExtra("collectCount", collectCount)
                .putExtra("followedCount", followedCount);
    }

    @Override
    public ResponseResult appletLogin(WechatAppletDTO wechatAppletDTO) {

        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wx3e9678e6cdabd38f&secret=f8f33e962ab232ab70fd6545b86ac731&js_code="+wechatAppletDTO.getCode()+"&grant_type=authorization_code";
        String result = HttpUtil.get(url);

        @SuppressWarnings("unchecked")
        Map<String, Object> resultMap = (Map<String, Object>) JSONUtil.toBean(result, Map.class);
        String openid = resultMap.get("openid").toString();
        UserInfoVO userInfoVO = this.wechatLogin(openid);

        StpUtil.login(userInfoVO.getId(), new SaLoginModel().setDevice("PC").setTimeout(60 * 60 * 24 * 7));
        userInfoVO.setToken(StpUtil.getTokenValueByLoginId(userInfoVO.getId()));
        return ResponseResult.success(userInfoVO);
    }


    private UserInfoVO wechatLogin(String openId) {
        UserInfoVO userInfo = userMapper.selectByUserName(openId);
        if (ObjectUtils.isEmpty(userInfo)) {
            String ip = IpUtil.getIp();
            String ipSource = IpUtil.getIp2region(ip);

            User user = User.builder()
                    .username(openId)
                    .password(aesEncryptUtil.aesEncrypt(openId))
                    .nickname("WECHAT-" + RandomUtil.generationCapital(6))
                    .avatar(randomAvatar())
                    .loginType(LoginTypeEnum.WECHAT.getType())
                    .lastLoginTime(DateUtil.getNowDate())
                    .ipAddress(ip)
                    .ipSource(ipSource)
                    .roleId(2)
                    .build();
            userMapper.insert(user);
            userInfo = BeanCopyUtil.copyObject(user,UserInfoVO.class);
        }
        return userInfo;
    }

    @NotNull
    private String randomAvatar() {
        return imagePrefix + userAvatarList[RandomUtil.generationNumber(userAvatarList.length)];
    }

}
