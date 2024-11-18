package com.zony.app.service.criteria;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.app.service.dto.UserDto;
import com.zony.common.annotation.Query;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class JurisdictionQueryCriteria {

    @Query
    private String id;

    @Query(blurry = "jurisdictionName",type = Query.Type.INNER_LIKE)
    private String blurry;//依据库文件名称

    @Query(type = Query.Type.BETWEEN)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private List<Timestamp> launchTime;//上传时间

    @Query(type = Query.Type.INNER_LIKE)
    private UserDto updatePerson;//提交人

    @Query(type = Query.Type.INNER_LIKE)
    private String companyName;

    @Query(type = Query.Type.EQUAL)
    private String  sequence;
}
