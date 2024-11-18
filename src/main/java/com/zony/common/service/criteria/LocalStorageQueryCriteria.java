
package com.zony.common.service.criteria;

import com.zony.common.annotation.Query;
import lombok.Data;
import java.sql.Timestamp;
import java.util.List;

@Data
public class LocalStorageQueryCriteria{

    @Query(blurry = "name,suffix,type,createBy,size")
    private String blurry;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}
