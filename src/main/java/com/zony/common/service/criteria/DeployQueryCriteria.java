
package com.zony.common.service.criteria;

import lombok.Data;
import com.zony.common.annotation.Query;
import java.sql.Timestamp;
import java.util.List;


@Data
public class DeployQueryCriteria{

	/**
	 * 模糊
	 */
    @Query(type = Query.Type.INNER_LIKE, propName = "name", joinName = "app")
    private String appName;

	@Query(type = Query.Type.BETWEEN)
	private List<Timestamp> createTime;

}
