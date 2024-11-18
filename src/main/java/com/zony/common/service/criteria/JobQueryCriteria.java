
package com.zony.common.service.criteria;

import lombok.Data;
import com.zony.common.annotation.Query;
import java.sql.Timestamp;
import java.util.List;

@Data
public class JobQueryCriteria {

    @Query(type = Query.Type.INNER_LIKE)
    private String jobName;

    @Query
    private Boolean isSuccess;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}
