package com.zony.app.service.criteria;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.app.domain.User;
import com.zony.common.annotation.Query;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class CollectQueryCriteria {
    private Long id;

    @Query(blurry = "systemTitle",type = Query.Type.INNER_LIKE)
    private String blurry;//制度名称

    @Query(type = Query.Type.INNER_LIKE)
    private String systemCode;

    @Query(type = Query.Type.INNER_LIKE)
    private String initUser;

    @Query(type = Query.Type.INNER_LIKE)
    private String initDept;

    @Query(type = Query.Type.EQUAL)
    private String username;
}
