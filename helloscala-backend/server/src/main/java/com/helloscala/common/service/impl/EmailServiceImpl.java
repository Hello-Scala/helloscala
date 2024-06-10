package com.helloscala.common.service.impl;

import com.helloscala.common.RedisConstants;
import com.helloscala.common.entity.FriendLink;
import com.helloscala.common.entity.SystemConfig;
import com.helloscala.common.service.EmailService;
import com.helloscala.common.service.RedisService;
import com.helloscala.common.service.SystemConfigService;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static com.helloscala.common.enums.FriendLinkEnum.DOWN;
import static com.helloscala.common.enums.FriendLinkEnum.UP;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final RedisService redisService;
    private final SystemConfigService systemConfigService;
    private final JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
    private final Map<Integer, Consumer<FriendLink>> map = new HashMap<>();

    @PostConstruct
    public void init(){
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();
        javaMailSender.setHost(systemConfig.getEmailHost());
        javaMailSender.setUsername(systemConfig.getEmailUsername());
        javaMailSender.setPassword(systemConfig.getEmailPassword());
        javaMailSender.setPort(systemConfig.getEmailPort());
        javaMailSender.setDefaultEncoding("UTF-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.debug", "true");
        javaMailSender.setJavaMailProperties(p);

        map.put(UP.getCode(),friendLink ->  friendPassSendEmail(friendLink.getEmail()));
        map.put(DOWN.getCode(),friendLink ->  friendFailedSendEmail(friendLink.getEmail(),friendLink.getReason()));
    }


    // todo email config
    @Async("threadPoolTaskExecutor")
    @Override
    public void emailNoticeMe(String subject,String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setFrom(Objects.requireNonNull(javaMailSender.getUsername()));
        message.setTo("stevenzearo@163.com");
        message.setSentDate(new Date());
        message.setText(content);
        javaMailSender.send(message);
    }

    @Override
    public void sendFriendEmail(FriendLink friendLink) {
        Consumer<FriendLink> consumer = map.get(friendLink.getStatus());
        if (consumer != null) {
            consumer.accept(friendLink);
        }
    }

    // todo file template
    @Override
    public void friendPassSendEmail(String email){
        String content = "<html>\n" +
                "<body>\n" +
                "    <p>您在"+"<a href='https://www.helloscala.com'>Hello Scala</a>"+"站点申请友链加入审核通过!!</span>\n" +
                "<p style='padding: 20px;'>感谢您的选择，本站将会竭尽维护好站点稳定，分享高质量的文章，欢迎相互交流互访。</p>" +
                "<p>可前往<a href='https://www.helloscala.com/links'>本站友链</a>查阅您的站点。</p></body>\n" +
                "</html>";
        try {
            send(email,content);
        }catch (Exception e){
            log.error("Failed to send email, email={}!", email, e);
        }
    }

    @Override
    public void friendFailedSendEmail(String email,String reason){
        String content = "<html>\n" +
                "<body>\n" +
                "    <p>您在"+"<a href='https://www.helloscala.com'>Hello Scala</a>"+"站点申请的友链加入审核未通过!具体原因为:"+ reason +"</span>\n" +
                "<p style='padding: 20px;'>感谢您的选择，本站将会竭尽维护好站点稳定，分享高质量的文章，欢迎相互交流互访。</p>" +
                "<p>可前往<a href='https://www.helloscala.com/links'>本站友链</a>查阅您的站点。</p></body>\n" +
                "</html>";
        try {
            send(email,content);
        }catch (Exception e){
            log.error("Failed to send email, email={}, reason={}!", email, reason, e);
        }
    }

    // todo template
    public void sendCode(String email) throws MessagingException {
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        String content = "<html>\n" +
                "\t<body><div id=\"contentDiv\" onmouseover=\"getTop().stopPropagation(event);\" onclick=\"getTop().preSwapLink(event, 'html', 'ZC0004_vDfNJayMtMUuKGIAzzsWvc8');\" style=\"position:relative;font-size:14px;height:auto;padding:15px 15px 10px 15px;z-index:1;zoom:1;line-height:1.7;\" class=\"body\">\n" +
                "  <div id=\"qm_con_body\">\n" +
                "    <div id=\"mailContentContainer\" class=\"qmbox qm_con_body_content qqmail_webmail_only\" style=\"opacity: 1;\">\n" +
                "      <style type=\"text/css\">\n" +
                "        .qmbox h1,.qmbox \t\t\th2,.qmbox \t\t\th3 {\t\t\t\tcolor: #00785a;\t\t\t}\t\t\t.qmbox p {\t\t\t\tpadding: 0;\t\t\t\tmargin: 0;\t\t\t\tcolor: #333;\t\t\t\tfont-size: 16px;\t\t\t}\t\t\t.qmbox hr {\t\t\t\tbackground-color: #d9d9d9;\t\t\t\tborder: none;\t\t\t\theight: 1px;\t\t\t}\t\t\t.qmbox .eo-link {\t\t\t\tcolor: #0576b9;\t\t\t\ttext-decoration: none;\t\t\t\tcursor: pointer;\t\t\t}\t\t\t.qmbox .eo-link:hover {\t\t\t\tcolor: #3498db;\t\t\t}\t\t\t.qmbox .eo-link:hover {\t\t\t\ttext-decoration: underline;\t\t\t}\t\t\t.qmbox .eo-p-link {\t\t\t\tdisplay: block;\t\t\t\tmargin-top: 20px;\t\t\t\tcolor: #009cff;\t\t\t\ttext-decoration: underline;\t\t\t}\t\t\t.qmbox .p-intro {\t\t\t\tpadding: 30px;\t\t\t}\t\t\t.qmbox .p-code {\t\t\t\tpadding: 0 30px 0 30px;\t\t\t}\t\t\t.qmbox .p-news {\t\t\t\tpadding: 0px 30px 30px 30px;\t\t\t}\n" +
                "      </style>\n" +
                "      <div style=\"max-width:800px;padding-bottom:10px;margin:20px auto 0 auto;\">\n" +
                "        <table cellpadding=\"0\" cellspacing=\"0\" style=\"background-color: #fff;border-collapse: collapse; border:1px solid #e5e5e5;box-shadow: 0 10px 15px rgba(0, 0, 0, 0.05);text-align: left;width: 100%;font-size: 14px;border-spacing: 0;\">\n" +
                "          <tbody>\n" +
                "            <tr style=\"background-color: #f8f8f8;\">\n" +
                "              <td>\n" +
                "                <img style=\"padding: 15px 0 15px 30px;width:50px\" src=\"\">" +
                "                <span>Hello Scala </span>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "              <td class=\"p-intro\">\n" +
                "                <h1 style=\"font-size: 26px; font-weight: bold;\">验证您的邮箱地址</h1>\n" +
                "                <p style=\"line-height:1.75em;\">感谢您使用 Hello Scala. </p>\n" +
                "                <p style=\"line-height:1.75em;\">以下是您的邮箱验证码，请将它输入到 Hello Scala 的邮箱验证码输入框中:</p>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "              <td class=\"p-code\">\n" +
                "                <p style=\"color: #253858;text-align:center;line-height:1.75em;background-color: #f2f2f2;min-width: 200px;margin: 0 auto;font-size: 28px;border-radius: 5px;border: 1px solid #d9d9d9;font-weight: bold;\">"+code+"</p>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "              <td class=\"p-intro\">\n" +
                "                <p style=\"line-height:1.75em;\">这一封邮件包括一些您的私密的 Hello Scala 账号信息，请不要回复或转发它，以免带来不必要的信息泄露风险。 </p>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "              <td class=\"p-intro\">\n" +
                "                <hr>\n" +
                "                <p style=\"text-align: center;line-height:1.75em;\">HelloScala - <a href='https://www.helloscala.com' style='text-decoration: none;color:#ff6574'>Hello Scala</a></p>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "          </tbody>\n" +
                "        </table>\n" +
                "      </div>\n" +
                "      <style type=\"text/css\">\n" +
                "        .qmbox style, .qmbox script, .qmbox head, .qmbox link, .qmbox meta {display: none !important;}\n" +
                "      </style>\n" +
                "    </div>\n" +
                "  </div><!-- -->\n" +
                "  <style>\n" +
                "    #mailContentContainer .txt {height:auto;}\n" +
                "  </style>\n" +
                "</div></body>\n" +
                "</html>\n";
       send(email,content);
       log.info("Send email success, email:{},code:{}",email,code);

       redisService.setCacheObject(RedisConstants.EMAIL_CODE+ email, code +"");
       redisService.expire(RedisConstants.EMAIL_CODE+ email,RedisConstants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);

    }

    private void send(String email, String template) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mineHelper = new MimeMessageHelper(mimeMessage, true);
        mineHelper.setSubject("Hello Scala");
        mineHelper.setFrom(Objects.requireNonNull(javaMailSender.getUsername()));
        mineHelper.setTo(email);
        mineHelper.setSentDate(new Date());
        mineHelper.setText(template,true);
        javaMailSender.send(mimeMessage);
    }
}
