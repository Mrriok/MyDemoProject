/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service.criteria;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.common.annotation.Query;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;


@Data
public class HelpQueryCriteria {
    @Query
    private Long id;

    @Query(blurry = "instName",type = Query.Type.INNER_LIKE)
    private String blurry;

    @Query(type = Query.Type.INNER_LIKE)
    private String name;//意见征求名称

    @Query(type = Query.Type.LESS_THAN)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp updateTime;

    @Query(type = Query.Type.EQUAL)
    private String type;// 文件种类



}
