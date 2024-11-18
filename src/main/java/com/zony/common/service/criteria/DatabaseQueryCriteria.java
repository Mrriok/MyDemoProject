
package com.zony.common.service.criteria;

import lombok.Data;
import com.zony.common.annotation.Query;
import java.sql.Timestamp;
import java.util.List;


@Data
public class DatabaseQueryCriteria{

	/**
	 * 模糊
	 */
    @Query(type = Query.Type.INNER_LIKE)
    private String name;

	/**
	 * 精确
	 */
    @Query
    private String jdbcUrl;

	@Query(type = Query.Type.BETWEEN)
	private List<Timestamp> createTime;
}
