
package com.zony.common.service;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.zony.common.annotation.Log;
import com.zony.common.repository.LogRepository;
import com.zony.common.service.criteria.LogQueryCriteria;
import com.zony.common.service.mapstruct.LogSmallMapper;
import com.zony.common.service.mapstruct.LogErrorMapper;
import com.zony.common.utils.*;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;


@Service
@RequiredArgsConstructor
public class LogService {
    private static final Logger log = LoggerFactory.getLogger(LogService.class);
    private final LogRepository logRepository;
    private final LogErrorMapper logErrorMapper;
    private final LogSmallMapper logSmallMapper;


    public Object queryAll(LogQueryCriteria criteria, Pageable pageable) {
        Page<com.zony.common.domain.Log> page = logRepository.findAll(((root, criteriaQuery, cb) -> QueryHelp.getPredicate(root, criteria, cb)), pageable);
        String status = "ERROR";
        if (status.equals(criteria.getLogType())) {
            return PageUtil.toPage(page.map(logErrorMapper::toDto));
        }
        return page;
    }


    public List<com.zony.common.domain.Log> queryAll(LogQueryCriteria criteria) {
        return logRepository.findAll(((root, criteriaQuery, cb) -> QueryHelp.getPredicate(root, criteria, cb)));
    }


    public Object queryAllByUser(LogQueryCriteria criteria, Pageable pageable) {
        Page<com.zony.common.domain.Log> page = logRepository.findAll(((root, criteriaQuery, cb) -> QueryHelp.getPredicate(root, criteria, cb)), pageable);
        return PageUtil.toPage(page.map(logSmallMapper::toDto));
    }


    @Transactional(rollbackFor = Exception.class)
    public void save(String username, String browser, String ip, ProceedingJoinPoint joinPoint, com.zony.common.domain.Log log) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log aopLog = method.getAnnotation(Log.class);

        // 方法路径
        String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";

        StringBuilder params = new StringBuilder("{");
        //参数值
        List<Object> argValues = new ArrayList<>(Arrays.asList(joinPoint.getArgs()));
        //参数名称
        for (Object argValue : argValues) {
            params.append(argValue).append(" ");
        }
        // 描述
        if (log != null) {
            log.setDescription(aopLog.value());
        }
        assert log != null;
        log.setRequestIp(ip);

        String loginPath = "login";
        if (loginPath.equals(signature.getName())) {
            try {
                username = new JSONObject(argValues.get(0)).get("username").toString();
            } catch (Exception e) {
                LogService.log.error(e.getMessage(), e);
            }
        }
        log.setAddress(StringUtils.getCityInfo(log.getRequestIp()));
        log.setMethod(methodName);
        log.setUsername(username);
        log.setParams(params.toString() + " }");
        log.setBrowser(browser);
        logRepository.save(log);
    }


    public Object findByErrDetail(Long id) {
        com.zony.common.domain.Log log = logRepository.findById(id).orElseGet(com.zony.common.domain.Log::new);
        ValidationUtil.isNull(log.getId(), "Log", "id", id);
        byte[] details = log.getExceptionDetail();
        return Dict.create().set("exception", new String(ObjectUtil.isNotNull(details) ? details : "".getBytes()));
    }


    public void download(List<com.zony.common.domain.Log> logs, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (com.zony.common.domain.Log log : logs) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", log.getUsername());
            map.put("IP", log.getRequestIp());
            map.put("IP来源", log.getAddress());
            map.put("描述", log.getDescription());
            map.put("浏览器", log.getBrowser());
            map.put("请求耗时/毫秒", log.getTime());
            map.put("异常详情", new String(ObjectUtil.isNotNull(log.getExceptionDetail()) ? log.getExceptionDetail() : "".getBytes()));
            map.put("创建日期", log.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }


    @Transactional(rollbackFor = Exception.class)
    public void delAllByError() {
        logRepository.deleteByLogType("ERROR");
    }


    @Transactional(rollbackFor = Exception.class)
    public void delAllByInfo() {
        logRepository.deleteByLogType("INFO");
    }
}
