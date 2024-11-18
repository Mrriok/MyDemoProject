
package com.zony.app.service;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import lombok.RequiredArgsConstructor;
import com.zony.common.domain.vo.EmailVo;
import com.zony.common.exception.BadRequestException;
import com.zony.common.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;


@Service
@RequiredArgsConstructor
public class VerifyService {

    @Value("${code.expiration}")
    private Long expiration;
    private final RedisUtils redisUtils;


    @Transactional(rollbackFor = Exception.class)
    public EmailVo sendEmail(String email, String key) {
        EmailVo emailVo;
        String content;
        String redisKey = key + email;
        // 如果不存在有效的验证码，就创建一个新的
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("email/email.ftl");
        Object oldCode =  redisUtils.get(redisKey);
        if(oldCode == null){
            String code = RandomUtil.randomNumbers (6);
            // 存入缓存
            if(!redisUtils.set(redisKey, code, expiration)){
                throw new BadRequestException("服务异常，请联系网站负责人");
            }
            content = template.render(Dict.create().set("code",code));
            emailVo = new EmailVo(Collections.singletonList(email),"CNOOC_SYSTEM",content);
        // 存在就再次发送原来的验证码
        } else {
            content = template.render(Dict.create().set("code",oldCode));
            emailVo = new EmailVo(Collections.singletonList(email),"CNOOC_SYSTEM",content);
        }
        return emailVo;
    }


    public void validated(String key, String code) {
        Object value = redisUtils.get(key);
        if(value == null || !value.toString().equals(code)){
            throw new BadRequestException("无效验证码");
        } else {
            redisUtils.del(key);
        }
    }
}
