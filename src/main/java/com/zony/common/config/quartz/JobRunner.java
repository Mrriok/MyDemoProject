
package com.zony.common.config.quartz;

import com.zony.common.repository.QuartzJobRepository;
import lombok.RequiredArgsConstructor;
import com.zony.common.domain.QuartzJob;
import com.zony.common.utils.QuartzManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JobRunner implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final QuartzJobRepository quartzJobRepository;
    private final QuartzManage quartzManage;

    /**
     * 项目启动时重新激活启用的定时任务
     * @param applicationArguments /
     */
    @Override
    public void run(ApplicationArguments applicationArguments){
        logger.info("--------------------注入定时任务---------------------");
        List<QuartzJob> quartzJobs = quartzJobRepository.findByIsPauseIsFalse();
        quartzJobs.forEach(quartzManage::addJob);
        logger.info("--------------------定时任务注入完成---------------------");
    }
}
