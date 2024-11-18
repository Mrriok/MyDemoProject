
package com.zony.common.service;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.zony.common.utils.EmailUtil;
import com.zony.common.domain.EmailConfig;
import com.zony.common.exception.BadRequestException;
import com.zony.common.utils.EncryptUtils;
import com.zony.common.domain.vo.EmailVo;
import com.zony.common.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "email")
public class EmailService {

    private final EmailRepository emailRepository;
    /**
     * 系统登录地址
     */
    @Value("${url.localhost}")
    private String loginUrl;
    
    @CachePut(key = "'id:1'")
    @Transactional(rollbackFor = Exception.class)
    public EmailConfig config(EmailConfig emailConfig, EmailConfig old) throws Exception {
        emailConfig.setId(1L);
        if(!emailConfig.getPass().equals(old.getPass())){
            // 对称加密
            emailConfig.setPass(EncryptUtils.desEncrypt(emailConfig.getPass()));
        }
        return emailRepository.save(emailConfig);
    }

    
    @Cacheable(key = "'id:1'")
    public EmailConfig find() {
        Optional<EmailConfig> emailConfig = emailRepository.findById(1L);
        return emailConfig.orElseGet(EmailConfig::new);
    }

    
    @Transactional(rollbackFor = Exception.class)
    public void send(EmailVo emailVo, EmailConfig emailConfig){
        if(emailConfig == null){
            throw new BadRequestException("请先配置，再操作");
        }
        // 发送
        try {
            EmailUtil.send(emailVo,emailConfig);
        }catch (Exception e){
            throw new BadRequestException(e.getMessage());
        }
    }


    /**
     * @Description: 流程待办邮件通知
     * @Date 2020/9/7 13:47
     * @Author ZLK
     * @param emailVo
     *          1. address：邮箱地址 支持多人
     *          2. title：邮件标题（例如：流程标题等）
     *          3. content：邮件正文，目前使用ftl模板
     *          4. param：map类型，基于不同场景自行定义
     */
    public void sendProcessAgentEmail(EmailVo emailVo){
        try {
            log.info("--------开始待办邮件通知--------");
            TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template/email", TemplateConfig.ResourceMode.CLASSPATH));
            Template template = engine.getTemplate("processAgent.ftl");
            Map<String,String> map = new HashMap<>();
            //当前处理人
            //map.put("handlerName",(String)emailVo.getParam().get("handlerName"));
            //发起/拟稿/提交人
            map.put("drafterName",(String)emailVo.getParam().get("drafterName"));
            //流程标题
            map.put("processTitle",emailVo.getTitle());
            //流程类型
            map.put("workflowTypeName", (String) emailVo.getParam().get("workflowTypeName"));
            //系统登录地址
            map.put("url",loginUrl);
            String content = template.render(map);
            emailVo.setContent(content);
            // 发送邮件
            EmailUtil.send(emailVo,find());
        }catch (Exception e){
            log.error("待办邮件通知失败：" + e.getMessage());
            throw new BadRequestException(e.getMessage());

        }
    }

    /**
     * @Description: 宣贯邮件通知
     * @Date 2020/9/7 13:47
     * @Author ZLK
     * @param emailVo
     *          1. address：邮箱地址 支持多人
     *          2. title：邮件标题（例如：流程标题等）
     *          3. content：邮件正文，目前使用ftl模板
     *          4. param：map类型，基于不同场景自行定义
     */
    public void sendPropagationEmail(EmailVo emailVo){
        try {
            log.info("--------开始待办邮件通知--------");
            TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template/email", TemplateConfig.ResourceMode.CLASSPATH));
            Template template = engine.getTemplate("announcement.ftl");
            Map<String,String> map = new HashMap<>();
            //当前处理人
            //map.put("handlerName",(String)emailVo.getParam().get("handlerName"));
            //发起/拟稿/提交人
            map.put("drafterName",(String)emailVo.getParam().get("drafterName"));
            //流程标题
            map.put("processTitle",emailVo.getTitle());
            //流程类型
            map.put("workflowTypeName", (String) emailVo.getParam().get("workflowTypeName"));
            //系统登录地址
            map.put("url",loginUrl);
            String content = template.render(map);
            emailVo.setContent(content);
            // 发送邮件
            EmailUtil.send(emailVo,find());
        }catch (Exception e){
            log.error("待办邮件通知失败：" + e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }

    /**
     * @Description: 编制计划截止提醒
     * @Date 2020/9/7 13:47
     * @Author ZLK
     * @param emailVo
     *          1. address：邮箱地址 支持多人
     *          2. title：邮件标题（例如：流程标题等）
     *          3. content：邮件正文，目前使用ftl模板
     *          4. param：map类型，基于不同场景自行定义
     */
    public void sendRegulationEmail(EmailVo emailVo){
        try {
            log.info("--------开始编制计划截止提醒邮件通知--------");
            TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template/email", TemplateConfig.ResourceMode.CLASSPATH));
            Template template = engine.getTemplate("regulationPlan.ftl");
            String content = template.render(emailVo.getParam());
            emailVo.setContent(content);
            // 发送邮件
            EmailUtil.send(emailVo,find());
        }catch (Exception e){
            log.error("待办邮件通知失败：" + e.getMessage());
            throw new BadRequestException(e.getMessage());

        }
    }

}
