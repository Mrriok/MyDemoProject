package com.zony.app.service.criteria;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.common.annotation.Query;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class MetadataQueryCriteria {

    @Query
    private String id;

    @Query(type = Query.Type.INNER_LIKE)
    private String risk;

    @Query(type = Query.Type.INNER_LIKE)
    private String target;

    @Query(type = Query.Type.INNER_LIKE)
    private String quote;
}
