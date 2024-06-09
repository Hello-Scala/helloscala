package com.helloscala.service.impl;

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
import com.helloscala.dto.WechatAppletDTO;
import com.helloscala.dto.user.EmailForgetPasswordDTO;
import com.helloscala.dto.user.EmailLoginDTO;
import com.helloscala.dto.user.EmailRegisterDTO;
import com.helloscala.dto.user.UserInfoDTO;
import com.helloscala.entity.Article;
import com.helloscala.entity.Collect;
import com.helloscala.entity.Followed;
import com.helloscala.entity.User;
import com.helloscala.enums.LoginTypeEnum;
import com.helloscala.enums.UserStatusEnum;
import com.helloscala.exception.BusinessException;
import com.helloscala.mapper.ArticleMapper;
import com.helloscala.mapper.CollectMapper;
import com.helloscala.mapper.FollowedMapper;
import com.helloscala.mapper.UserMapper;
import com.helloscala.service.ApiUserService;
import com.helloscala.service.EmailService;
import com.helloscala.service.RedisService;
import com.helloscala.utils.AesEncryptUtil;
import com.helloscala.utils.BeanCopyUtil;
import com.helloscala.utils.DateUtil;
import com.helloscala.utils.IpUtil;
import com.helloscala.utils.RandomUtil;
import com.helloscala.vo.user.UserInfoVO;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.zhyd.oauth.model.AuthResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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

    private final UserMapper userMapper;

    private final ArticleMapper articleMapper;

    private final CollectMapper collectMapper;

    private final RedisService redisService;

    private final FollowedMapper followedMapper;

    private final EmailService emailService;


    private final ObjectMapper objectMapper = new ObjectMapper();


    private final String[] userAvatarList = {"/asserts/avatars/buxie.png","/asserts/avatars/daizhi.png",
            "/asserts/avatars/fennu.png","/asserts/avatars/jingxi.png","/asserts/avatars/kaixin.png",
            "/asserts/avatars/shuashuai.png"};


    /**
     * 邮箱登录
     *
     * @param vo
     * @return
     */
    @Override
    public ResponseResult emailLogin(EmailLoginDTO vo) {

        User user = userMapper.selectNameAndPassword(vo.getEmail(), AesEncryptUtil.aesEncrypt(vo.getPassword()));
        if (user == null) {
            throw new BusinessException(ERROR_PASSWORD.desc);
        }

        if (user.getStatus() == UserStatusEnum.disable.code) {
            throw new BusinessException(DISABLE_ACCOUNT.desc);
        }

        //七天有效时间  登录
        StpUtil.login(user.getId(), new SaLoginModel().setDevice("PC").setTimeout(60 * 60 * 24 * 7));

        //组装数据
        UserInfoVO userInfoVO = BeanCopyUtil.copyObject(user, UserInfoVO.class);
        userInfoVO.setToken(StpUtil.getTokenValueByLoginId(user.getId()));

        StpUtil.getSession().set(Constants.CURRENT_USER,userMapper.getById(user.getId()));

        return ResponseResult.success(userInfoVO);
    }

    /**
     * 判断用户是否微信登录成功
     *
     * @param loginCode 用户临时id
     * @return
     */
    @Override
    public ResponseResult wxIsLogin(String loginCode) {
        Object value =redisService.getCacheObject(RedisConstants.WX_LOGIN_USER + loginCode);

        if (value == null) {
            return ResponseResult.error("用户未被授权");
        }
        UserInfoVO user = objectMapper.convertValue(value, UserInfoVO.class);
        StpUtil.login(user.getId(), new SaLoginModel().setDevice("PC").setTimeout(60 * 60 * 24 * 7));
        user.setToken(StpUtil.getTokenValueByLoginId(user.getId()));
        return ResponseResult.success(user);
    }

    /**
     * 微信扫码公众号登录
     * @param message
     * @return
     */
    @Override
    public String wechatLogin(WxMpXmlMessage message) {
        String content = message.getContent().toUpperCase();
        //先判断登录码是否已过期
        boolean loginFlag = redisService.hasKey(RedisConstants.WX_LOGIN_USER_STATUE + content);
        if (!loginFlag) {
            return "验证码已过期";
        }
        UserInfoVO userInfoVO = wechatLogin(message.getFromUser());


        //修改redis缓存 以便监听是否已经授权成功
        redisService.setCacheObject(RedisConstants.WX_LOGIN_USER + content,userInfoVO, 60, TimeUnit.SECONDS);
        return "网站登录成功！(若页面长时间未跳转请刷新验证码)";
    }



    /**
     * 获取微信公众号登录验证码
     *
     * @return
     */
    @Override
    public ResponseResult getWechatLoginCode() {
        String code = "DL" + RandomUtil.generationNumberChar(4);
        redisService.setCacheObject(RedisConstants.WX_LOGIN_USER_STATUE + code, false, 60, TimeUnit.SECONDS);
        return ResponseResult.success(code);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @Override
    public ResponseResult selectUserInfo(String userId) {
        userId = StringUtils.isNotBlank(userId) ? userId : StpUtil.getLoginIdAsString();
        UserInfoVO userInfo = userMapper.selectInfoByUserId(userId);
        return ResponseResult.success(userInfo);
    }

    /**
     * 修改用户信息
     *
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateUser(UserInfoDTO vo) {
        User user = userMapper.selectById(StpUtil.getLoginIdAsString());
        if (ObjectUtils.isEmpty(user)) {
            throw  new BusinessException("用户不存在");
        }
        user = BeanCopyUtil.copyObject(vo, User.class);
        user.setId(StpUtil.getLoginIdAsString());
        userMapper.updateById(user);

        return ResponseResult.success();
    }

    /**
     * 根据token获取用户信息
     *
     * @param token
     * @return
     */
    @Override
    public ResponseResult selectUserInfoByToken(String token) {
        Object userId = StpUtil.getLoginIdByToken(token);
        UserInfoVO userInfoVO = userMapper.selectInfoByUserId(userId);
        return ResponseResult.success(userInfoVO);
    }

    /**
     * 第三方登录授权之后的逻辑
     *
     * @param response
     * @param source
     * @param httpServletResponse
     * @throws IOException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void authLogin(AuthResponse response, String source,  HttpServletResponse httpServletResponse) throws IOException {
        if (response.getData() == null) {
            log.info("用户取消了 {} 第三方登录",source);
            httpServletResponse.sendRedirect("https://www.helloscala.com");
            return;
        }
        String result = JSONUtil.toJsonStr(response.getData());
        log.info("第三方登录验证结果:{}", result);

        Map<String, Object> paramMap = JSONUtil.toBean(result, Map.class);
        Object uuid = paramMap.get("uuid");
        // 获取用户ip信息
        String ipAddress = IpUtil.getIp();
        String ipSource = IpUtil.getIp2region(ipAddress);
        // 判断是否已注册
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, uuid));
        if (Objects.nonNull(user)) {
            // 更新登录信息
            userMapper.update(new User(), new LambdaUpdateWrapper<User>()
                    .set(User::getLastLoginTime, LocalDateTime.now())
                    .set(User::getIpAddress, ipAddress)
                    .set(User::getIpSource, ipSource)
                    .eq(User::getId, user.getId()));
        } else {
            // 保存账号信息
            user = User.builder()
                    .username(uuid.toString())
                    .password(UUID.randomUUID().toString())
                    .loginType(LoginTypeEnum.getType(source))
                    .lastLoginTime(com.helloscala.utils.DateUtil.getNowDate())
                    .ipAddress(ipAddress)
                    .ipSource(ipSource)
                    .roleId(2)
                    .status(UserStatusEnum.normal.getCode())
                    .nickname(source.equals("github") ? paramMap.get("username").toString() : paramMap.get("nickname").toString())
                    .avatar(paramMap.get("avatar").toString())
                    .build();
            userMapper.insert(user);
        }

        StpUtil.login(user.getId(), new SaLoginModel().setDevice("PC").setTimeout(60 * 60 * 24 * 7));
        httpServletResponse.sendRedirect("https://www.helloscala.com/?token=" + StpUtil.getTokenValueByLoginId(user.getId()));
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
        //校验邮箱验证码
        boolean b = redisService.hasKey(RedisConstants.EMAIL_CODE + emailRegisterDTO.getEmail());
        if (!b) {
            throw new BusinessException(ResultCode.ERROR_EXCEPTION_MOBILE_CODE);
        }
        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getUsername, emailRegisterDTO.getEmail()));
        if (count > 0) {
            throw new BusinessException("Email registered, email={}!", emailRegisterDTO.getEmail());
        }
        // 保存账号信息
        User user = User.builder()
                .username(emailRegisterDTO.getEmail())
                .password(AesEncryptUtil.aesEncrypt(emailRegisterDTO.getPassword()))
                .loginType(LoginTypeEnum.getType("email"))
                .roleId(2)
                .status(UserStatusEnum.normal.getCode())
                .nickname(emailRegisterDTO.getNickname())
                .build();
        userMapper.insert(user);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult forgetPassword(EmailForgetPasswordDTO emailForgetPasswordDTO) {
        //校验邮箱验证码
        boolean b = redisService.hasKey(RedisConstants.EMAIL_CODE + emailForgetPasswordDTO.getEmail());
        if (!b) {
            throw new BusinessException(ResultCode.ERROR_EXCEPTION_MOBILE_CODE);
        }
        User user = User.builder().password(AesEncryptUtil.aesEncrypt(emailForgetPasswordDTO.getPassword())).build();
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

        Map<String, Object> resultMap = JSONUtil.toBean(result, Map.class);
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

            // 保存账号信息
            User user = User.builder()
                    .username(openId)
                    .password(AesEncryptUtil.aesEncrypt(openId))
                    .nickname("WECHAT-" + RandomUtil.generationCapital(6))
                    .avatar(userAvatarList[RandomUtil.generationNumber(userAvatarList.length)])
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

}
