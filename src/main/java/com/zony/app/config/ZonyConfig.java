package com.zony.app.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource(value = "classpath:config/zony_config.yml", factory = com.zony.app.config.ResourceFactory.class)
public class ZonyConfig {

    @Value("${system.user.name}")
    private String name;
    @Value("${system.user.password}")
    private String password;

    @Value("${filenet.ADMIN}")
    private String fnAdmin;
    @Value("${filenet.PWD}")
    private String fnPassword;
    @Value("${filenet.ContentEngineURL}")
    private String fnCeUrl;
    @Value("${filenet.JAASStanzaName}")
    private String fnJAASStanzaName;
    @Value("${filenet.OS}")
    private String fnObjectStoreName;
    @Value("${filenet.DOMAIN_NAME}")
    private String fnDomainName;
}
