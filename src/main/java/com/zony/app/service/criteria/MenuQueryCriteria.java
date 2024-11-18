
package com.zony.app.service.criteria;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.zony.common.annotation.Query;
import java.sql.Timestamp;
import java.util.List;

/**
 * 公共查询类
 */
@Data
public class MenuQueryCriteria {

    @Query(blurry = "title,component,permission")
    private String blurry;

    @Query(type = Query.Type.BETWEEN)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private List<Timestamp> createTime;

    @Query(type = Query.Type.IS_NULL, propName = "pid")
    private Boolean pidIsNull;

    @Query
    private Long pid;
}
