/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service.criteria;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.app.enums.InstLevelEnum;
import com.zony.app.enums.InstPropertyEnum;
import com.zony.app.enums.ProgressStatusEnum;
import com.zony.common.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Convert;
import java.sql.Timestamp;
import java.util.List;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/15 -13:27
 */
@Data
public class OpinionQueryCriteria {

    @Query
    private Long id;

    @Query(blurry = "instName,opinionName",type = Query.Type.INNER_LIKE)
    private String blurry;

    @Query(type = Query.Type.INNER_LIKE)
    private String opinionName;//意见征求名称

    //@Query(type = Query.Type.INNER_LIKE)
    //private String draftDeptName; //起草部门名称

    @Query(type = Query.Type.INNER_LIKE)
    private String instName;//制度名称

    private Long instCode;//制度编码

    @Query(type = Query.Type.EQUAL)
    @Convert(converter = InstLevelEnum.EnumConverter.class)
    private InstLevelEnum instLevel;//制度层级

    @Query(type = Query.Type.EQUAL)
    @Convert(converter = InstPropertyEnum.EnumConverter.class)
    private InstPropertyEnum instProperty;//制度属性

    @Query(type = Query.Type.BETWEEN)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private List<Timestamp> deadline;//截止时间

    @Query(type = Query.Type.EQUAL)
    @Convert(converter = ProgressStatusEnum.EnumConverter.class)
    private String status;// 状态

    //jsonString
    @Query(type = Query.Type.INNER_LIKE)
    private String contactPersonIds;
    //@Query(type = Query.Type.EQUAL)
    //private Long deptId;//制度所属部门id
}
