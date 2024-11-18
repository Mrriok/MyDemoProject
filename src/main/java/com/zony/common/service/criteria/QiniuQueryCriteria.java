
package com.zony.common.service.criteria;

import com.zony.common.annotation.Query;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class QiniuQueryCriteria{

    @Query(type = Query.Type.INNER_LIKE)
    private String key;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}
