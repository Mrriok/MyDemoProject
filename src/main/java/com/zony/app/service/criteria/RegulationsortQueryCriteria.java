package com.zony.app.service.criteria;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.zony.common.annotation.DataPermission;
import com.zony.common.annotation.Query;
import java.sql.Timestamp;
import java.util.List;

@Data
public class RegulationsortQueryCriteria {

    @Query
    private String id;

    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    @Query
    private Boolean enabled;

    @Query
    private Long pid;

    @Query
    private String menuId;

    @Query(type = Query.Type.IS_NULL, propName = "pid")
    private Boolean pidIsNull;
}
