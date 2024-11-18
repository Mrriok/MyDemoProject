package com.zony.app.service.criteria;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.app.domain.User;
import com.zony.common.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class BaselibraryQueryCriteria {

    @Query
    private String id;

    @Query(blurry = "baselibraryName",type = Query.Type.INNER_LIKE)
    private String blurry;//权限手册名称

    @Query(type = Query.Type.BETWEEN)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private List<Timestamp> launchTime;//更新时间

    @Query(type = Query.Type.INNER_LIKE)
    private String uploaderName;//上传人

    @Query(type = Query.Type.INNER_LIKE)
    private String uploaderId;

    //@Query(type = Query.Type.EQUAL)
    //private Long systemId;

    @Query(type = Query.Type.EQUAL)
    private Boolean flag;
}
