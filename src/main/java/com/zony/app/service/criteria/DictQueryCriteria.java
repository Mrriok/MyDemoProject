
package com.zony.app.service.criteria;

import lombok.Data;
import com.zony.common.annotation.Query;

/**
 * 公共查询类
 */
@Data
public class DictQueryCriteria {

    @Query(blurry = "name,description")
    private String blurry;
}
