/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service.criteria;

import com.zony.common.annotation.Query;
import lombok.Data;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/9/18 -13:51
 */
@Data
public class DocReplyRiskTargetQueryCriteria {
    @Query
    private Long id;

    @Query(blurry = "value",type = Query.Type.INNER_LIKE)
    private String blurry;

}
