package com.zony.app.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zony.common.service.dto.AuthUserDto;
import com.zony.common.utils.RedisUtils;
import com.zony.common.utils.RsaUtils;
import org.junit.Before;
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
public class EstablishmentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RedisUtils redisUtils;

    private String token = "";
    private String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANL378k3RiZHWx5AfJqdH9xRNBmD9wGD" +
            "2iRe41HdTNF8RUhNnHit5NpMNtGL0NPTSSpPjjI1kJfVorRvaQerUgkCAwEAAQ==";

    @Before
    public void setUp() throws Exception {
        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setUsername("admin");
        authUserDto.setPassword(RsaUtils.encryptByPublicKey(publicKey, "123456"));

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

        System.out.println("########################authUserDto:"+authUserDto.toString());
        mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(JSON.toJSONString(authUserDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
         .andReturn();
        response = mvcResult.getResponse();
        token = (String) JSON.parseObject(response.getContentAsString()).get("token");
        //System.out.println(response.getStatus());
        //System.out.println(response.getContentAsString());
    }


        @Test
    public void query() throws Exception {
        String url = "/api//establishment?page=0&size=10&sort=dateOfWriting%2Casc&blurry=8";
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .contentType("application/json;charset=UTF-8")
                        .header("Authorization",token)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        System.out.println(response.getStatus());
        System.out.println(JSONArray.parse(response.getContentAsString()));
    }

    @Test
    public void view() {
    }

    @Test
    public void create() {
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void operating() {
    }
}