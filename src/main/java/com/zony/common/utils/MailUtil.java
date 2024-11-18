package com.zony.common.utils;

import com.zony.common.domain.vo.EmailVo;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MailUtil {
    //发送者邮箱账号
    private final static String USERNAME ="";
    //发送者邮箱密码
    private final static String PASSWORD ="";
    //SMTP服务器
    private final static String SMTPHOST ="smtp.zonysoft.com";
    //SMTP服务器端口
    private final static String SMTPPORT ="25";

    /**
     * 执行发送邮件
     * @throws Exception 如果发送失败会抛出异常信息
     */
    public void send(EmailVo mail) throws Exception
    {


        if(mail.getTitle() == null || mail.getTitle() .trim().length() == 0)
        {
            throw new Exception("邮件标题没有设置.调用title方法设置");
        }

        if(mail.getContent() == null || mail.getContent().trim().length() == 0)
        {
            throw new Exception("邮件内容没有设置.调用content方法设置");
        }

        if(mail.getAddress().size() == 0)
        {
            throw new Exception("没有接受者邮箱地址.调用targets方法设置");
        }

        // 创建Properties 类用于记录邮箱的一些属性
        final Properties props = new Properties();
        // 表示SMTP发送邮件，必须进行身份验证
        props.put("mail.smtp.auth", "true");
        //此处填写SMTP服务器
        props.put("mail.smtp.host", SMTPHOST);
        //设置端口号，QQ邮箱给出了两个端口465/587
        props.put("mail.smtp.port", SMTPPORT);
        // 设置发送邮箱
        props.put("mail.user",USERNAME);
        // 设置发送邮箱的16位STMP口令
        props.put("mail.password", PASSWORD);

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = USERNAME;
                String password = PASSWORD;
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        // 设置发件人
        String nickName = MimeUtility.encodeText(USERNAME);
        InternetAddress form = new InternetAddress(nickName + " <" + props.getProperty("mail.user") + ">");
        message.setFrom(form);

        // 设置邮件标题
        message.setSubject(mail.getTitle());

            message.setText(mail.getContent());

        //发送邮箱地址
        List<String> targets = mail.getAddress();
        for(int i = 0;i < targets.size();i++){
            try {
                // 设置收件人的邮箱
                InternetAddress to = new InternetAddress(targets.get(i));
                message.setRecipient(Message.RecipientType.TO, to);
                // 最后当然就是发送邮件啦
                Transport.send(message);
            }catch (Exception e)
            {
                continue;
            }

        }
    }
    public static  void main(String[] args) throws Exception {
        EmailVo emailVo=new EmailVo();
        emailVo.setContent("邮件测试正文");
        emailVo.setTitle("邮件测试标题");

        List<String> list=new ArrayList();
        list.add("zhujh@zonysoft.com");
        emailVo.setAddress(list);
        new MailUtil().send(emailVo);
    }
}
