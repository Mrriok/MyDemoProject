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

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/8 -15:55
 */
@Data
@NoArgsConstructor
public class NoticeQueryCriteria {

    @Query(type = Query.Type.EQUAL)
    private Long id;

    @Query(blurry = "title,content,mark",type = Query.Type.INNER_LIKE)
    private String blurry;

    @Query(type = Query.Type.LESS_THAN)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp effectiveTime;

    //@Query(type = Query.Type.EQUAL)
    //private String documentId;
}
