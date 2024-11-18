package com.zony.app.service.criteria;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.app.domain.Regulationsort;
import com.zony.app.domain.User;
import com.zony.app.enums.InstLevelEnum;
import com.zony.app.enums.InstPropertyEnum;
import com.zony.app.enums.ProgressStatusEnum;
import com.zony.common.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.util.List;

@Data
public class RegulationQueryCriteria {

    @Query
    private Long id;

    @Query(blurry = "instName",type = Query.Type.INNER_LIKE)
    private String blurry;//制度名称

    @Query(type = Query.Type.INNER_LIKE)
    private String instName;

    @Query(type = Query.Type.INNER_LIKE)
    private String instCode;

    @Query(type = Query.Type.EQUAL)
    private User initUser;

    @Query(type = Query.Type.INNER_LIKE)
    private String reference;

    @Query(type = Query.Type.INNER_LIKE)
    private String abolish;

    @Query(type = Query.Type.EQUAL)
    private ProgressStatusEnum status;

    @Query(type = Query.Type.EQUAL)
    private InstPropertyEnum instProperty;

    @Query(type = Query.Type.EQUAL)
    private InstLevelEnum instLevel;

    @Query(type = Query.Type.EQUAL)
    private Regulationsort regulationsort;

    @Query(type = Query.Type.BETWEEN)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private List<Timestamp> expectedTime;//预计完成时间

    @Query(type = Query.Type.EQUAL)
    private Long deptId;//制度所属部门id

    //@Query(type = Query.Type.EQUAL)
    private Integer page;//流程编号

    //@Query(type = Query.Type.EQUAL)
    private Integer size;//流程编号

    //@Query(type = Query.Type.EQUAL)
    private List<String> sort;//流程编号

    //@Query(type = Query.Type.IN)
    //private List<Long> deptIdList;
}
