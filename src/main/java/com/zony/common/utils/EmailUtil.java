package com.zony.common.utils;

import com.zony.common.domain.EmailConfig;
import com.zony.common.domain.vo.EmailVo;
import com.zony.common.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import javax.mail.internet.MimeMessage;

/**
 * @Description: 邮件工具类
 * @Date 2020-09-07
 * EmailConfig 配置在数据库
 * @Author ZLK
 */
@Slf4j
@PropertySource(value = "classpath:config/zony_config.yml", factory = com.zony.app.config.ResourceFactory.class)
public class EmailUtil {

    /**
     * @Description: 统一邮箱通知方法
     * @Date 2020/9/7 13:29
     * @Author ZLK
     * @param emailVi
     *          1. address：邮箱地址
     *          2. title：邮件标题（例如：流程标题等）
     * @param emailConfig 邮箱配置
     * @return
     */
    public static void send(EmailVo emailVi, EmailConfig emailConfig){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(emailConfig.getHost());
        javaMailSender.setPort(Integer.parseInt(emailConfig.getPort()));
        javaMailSender.setUsername(emailConfig.getUser());
        javaMailSender.setPassword(emailConfig.getPass());
        javaMailSender.setDefaultEncoding("UTF-8");
        // 发送邮件
        try {
            MimeMessage message=javaMailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setFrom(emailConfig.getUser());
            //支持多人
            helper.setTo(emailVi.getAddress().toArray(new String[emailVi.getAddress().size()]));
            helper.setSubject(emailVi.getTitle());
            helper.setText(emailVi.getContent(),true);
            javaMailSender.send(message);
            log.info("--------邮件通知处理成功--------");
        }catch (Exception e){
            log.error("邮件通知失败：" + e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }
}
