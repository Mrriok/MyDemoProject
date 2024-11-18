package com.zony.app.service.criteria;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.app.enums.InstStatusEnum;
import com.zony.app.enums.InstStatusTypeEnum;
import com.zony.app.enums.WorkflowDisposeObjEnum;
import com.zony.common.annotation.Query;
import lombok.Data;

import javax.persistence.Convert;
import java.sql.Timestamp;
import java.util.List;

@Data
public class SystemDocumentQueryCrietria {
    @Query
    private Long id;

    @Query(blurry = "systemTitle",type = Query.Type.INNER_LIKE)
    private String blurry;//制度名称

    @Query(type = Query.Type.INNER_LIKE)
    private String systemCode;//制度编码

    @Query(type = Query.Type.INNER_LIKE)
    private String draftDept;//牵头起草部门

    @Query(type = Query.Type.BETWEEN)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private List<Timestamp>ffectiveDate;//生效时间

    @Query(type = Query.Type.EQUAL)
    @Convert(converter = InstStatusEnum.EnumConverter.class)
    private InstStatusEnum instStatus;//制度状态

    @Query(type = Query.Type.EQUAL)
    @Convert(converter = InstStatusTypeEnum.EnumConverter.class)
    private InstStatusTypeEnum instStatusType;//制度状态

    @Query(type = Query.Type.EQUAL)
    private Long deptId;//制度所属部门id
}
