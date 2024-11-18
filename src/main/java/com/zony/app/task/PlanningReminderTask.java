package com.zony.app.task;


import com.zony.app.domain.DictDetail;
import com.zony.app.domain.Regulation;
import com.zony.app.enums.ProgressStatusEnum;
import com.zony.app.repository.DictDetailRepository;
import com.zony.app.repository.RegulationRepository;
import com.zony.app.repository.UserRepository;
import com.zony.app.service.RegulationService;
import com.zony.app.utils.DateUtils;
import com.zony.common.domain.vo.EmailVo;
import com.zony.common.service.EmailService;
import com.zony.common.utils.QueryHelp;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlanningReminderTask{

    private static Logger log = LoggerFactory.getLogger(PlanningReminderTask.class);
    private final RegulationRepository regulationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final DictDetailRepository dictDetailRepository;

    @Value("${url.localhost}")
    private String url;

    public void doTask(){
        List<Timestamp> expectedTimeList = new ArrayList<>();
        List<DictDetail> dictDetailList = dictDetailRepository.findByDictName("plan_reminder_time");
        StringBuilder time = new StringBuilder();
        for (DictDetail item : dictDetailList){
            if ("plan_reminder_time".equals(item.getLabel())) {
                time.append(item.getValue());
                break;
            }
        }
        expectedTimeList.add(new Timestamp(DateUtils.getToday(DateUtils.YYYY_MM_DD).getTime() + Long.parseLong(time.toString())));
        expectedTimeList.add(new Timestamp(DateUtils.getToday(DateUtils.YYYY_MM_DD).getTime() + Long.parseLong(time.toString()) + 24*60*60*1000));

        List<Regulation> regulationList = regulationRepository.findAllByExpectedTimeAndStatus(expectedTimeList.get(0),expectedTimeList.get(1),ProgressStatusEnum.COMPLETE);
        if (!CollectionUtils.isEmpty(regulationList)){
            regulationList.forEach(item->{
                toEmail(item);
            });
        }
    }

    @Async
    public void toEmail(Regulation regulation) {
        log.info("======PlanningReminderTask start==========");
        EmailVo emailVo = new EmailVo();
        Map<String,Object> paramMap = new HashMap<>();
        List<String> address = new ArrayList<>();
        address.add(regulation.getInitUser().getEmail());

        emailVo.setTitle(regulation.getInstName()+"时间截止邮件提醒");
        emailVo.setAddress(address);
        paramMap.put("url",url);
        paramMap.put("nickName",regulation.getInitUser().getNickName());
        paramMap.put("instName",regulation.getInstName());
        emailVo.setParam(paramMap);
        emailService.sendRegulationEmail(emailVo);
        log.info("======PlanningReminderTask end==========");
    }
}
