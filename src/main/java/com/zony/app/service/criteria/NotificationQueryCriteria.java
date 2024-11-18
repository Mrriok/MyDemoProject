/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service.criteria;

import com.zony.common.annotation.Query;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/13 -10:03 
 */
@Data
@NoArgsConstructor
public class NotificationQueryCriteria {

    @Query(type = Query.Type.EQUAL)
    private Long id;

    @Query(blurry = "messageTitle,messageContent",type = Query.Type.INNER_LIKE)
    private String blurry;

    @Query(type = Query.Type.INNER_LIKE)
    private String toSomeone;

    @Query(type = Query.Type.GREATER_THAN)
    private Timestamp sentTime;

    @Query(type = Query.Type.EQUAL)
    private String createBy;

    @Query(type = Query.Type.EQUAL)
    private Boolean readFlag;

    @Query(type = Query.Type.EQUAL)
    private Boolean isDelete;

    private String listFlag;
}
