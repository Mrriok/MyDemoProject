package com.zony.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Set;

/**
 * @Description: 系统重启清空缓存配置
 * @Date 2020-03-12
 * @Author CQH
 */
@Slf4j
@Component
public class CacheUpdateRunner implements CommandLineRunner {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void run(String... args) throws RuntimeException {
        //获取所有key
        Set<String> keys = redisTemplate.keys("*");
        //迭代删除所有
        Iterator<String> it1 = keys.iterator();
        while (it1.hasNext()) {
            redisTemplate.delete(it1.next());
        }
        log.info("======== PS:为防止持久缓存导致系统异常，所以每次系统重启，将清空所有缓存Redis keys * ========");
        log.info("=================================== 清除成功 ======================================");
    }
}
