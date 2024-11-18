
package com.zony.common.service.criteria;

import lombok.Data;
import com.zony.common.annotation.Query;
import java.sql.Timestamp;
import java.util.List;


@Data
public class DeployHistoryQueryCriteria{

	/**
	 * 精确
	 */
	@Query(blurry = "appName,ip,deployUser")
	private String blurry;

	@Query
	private Long deployId;

	@Query(type = Query.Type.BETWEEN)
	private List<Timestamp> deployDate;
}
