
package com.zony.app.service.criteria;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.zony.common.annotation.DataPermission;
import com.zony.common.annotation.Query;
import java.sql.Timestamp;
import java.util.List;


@Data
@DataPermission(fieldName = "id")
public class DeptQueryCriteria{

    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    @Query
    private Boolean enabled;

    @Query
    private Long pid;

    @Query(type = Query.Type.IS_NULL, propName = "pid")
    private Boolean pidIsNull;

    @Query(type = Query.Type.BETWEEN)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private List<Timestamp> createTime;

    //是否需要pid部门信息，true需要，false不需要
    private Boolean listFlag;
}
