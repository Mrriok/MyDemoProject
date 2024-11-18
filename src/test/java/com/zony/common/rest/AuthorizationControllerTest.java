package com.zony.common.rest;

import com.alibaba.fastjson.JSON;
import com.zony.common.service.dto.AuthUserDto;
import com.zony.common.utils.RedisUtils;
import com.zony.common.utils.RsaUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;

/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthorizationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RedisUtils redisUtils;

    private String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANL378k3RiZHWx5AfJqdH9xRNBmD9wGD" +
            "2iRe41HdTNF8RUhNnHit5NpMNtGL0NPTSSpPjjI1kJfVorRvaQerUgkCAwEAAQ==";
    @Test
    public void login() throws Exception {

        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setUsername("admin");
        authUserDto.setPassword(RsaUtils.encryptByPublicKey(publicKey, "123456"));

        //Map<String,String> body = new HashMap<>();
        //body.put("username","admin");
        //body.put("password", RsaUtils.encryptByPublicKey(publicKey, "Admin1234"));

        //获取验证码中redis的key
        String url = "/auth/code";
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String uuid = (String) JSON.parseObject(response.getContentAsString(), HashMap.class).get("uuid");
        String code = (String) redisUtils.get(uuid);
        System.out.println("########################uuid:"+uuid);
        System.out.println("########################code:"+code);

        url = "/auth/login";
        authUserDto.setUuid(uuid);
        authUserDto.setCode(code);
        //body.put("uuid",uuid);
        //body.put("code",code);
        //System.out.println("########################authUserDto:"+body.toString());
        System.out.println("########################authUserDto:"+authUserDto.toString());
        mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(JSON.toJSONString(authUserDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
         .andReturn();
        response = mvcResult.getResponse();
        System.out.println(response.getStatus());
        System.out.println(response.getContentAsString());
    }

    @Test
    public void getCode() throws Exception {
        String url = "/auth/code";
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        System.out.println(response.getStatus());
        String uuid = (String) JSON.parseObject(response.getContentAsString(), HashMap.class).get("uuid");
        System.out.println(uuid);
    }
}