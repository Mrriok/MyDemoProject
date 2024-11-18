package com.zony.common.event;

import com.zony.app.config.ZonyConfig;
import com.zony.common.filenet.util.FnConfigOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class GlobalAnnotationDrivenListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ZonyConfig zonyConfig;

    @EventListener
    public void handleApplicationStarted(ApplicationStartedEvent ase) {
        logger.debug("Handling application {} started event.", ase);
        logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>服务启动完成！");
        logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        // FileNet配置初始化
        FnConfigOptions.init(zonyConfig);
    }

}
