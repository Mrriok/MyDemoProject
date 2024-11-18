/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service.criteria;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.app.enums.InstStatusEnum;
import com.zony.common.annotation.Query;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/30 -14:24
 */
@Data
public class EstablishmentQueryCriteria {

    @Query
    private Long id;

    @Query(blurry = "instName,instCode",type = Query.Type.INNER_LIKE)
    private String blurry;

    /**
     * 制度编号
     */
    @Query(type = Query.Type.INNER_LIKE)
    private String instCode;

    /**
     * 制度名称
     */
    @Query(type = Query.Type.INNER_LIKE)
    private String instName;

    /**
     * 编写部门
     */
    @Query(type = Query.Type.INNER_LIKE)
    private String writingDeptName;

    /**
     * 编写日期
     */
    @Query(type = Query.Type.BETWEEN)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private List<Timestamp> dateOfWritingList;//截止时间

    /**
     * 状态
     */
    @Query(type = Query.Type.EQUAL)
    private InstStatusEnum status;

}
