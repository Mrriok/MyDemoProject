
package com.zony.app.service.criteria;

import lombok.Data;
import com.zony.common.annotation.Query;


@Data
public class DictDetailQueryCriteria {

    @Query(type = Query.Type.INNER_LIKE)
    private String label;

    @Query(propName = "name",joinName = "dict")
    private String dictName;

}
