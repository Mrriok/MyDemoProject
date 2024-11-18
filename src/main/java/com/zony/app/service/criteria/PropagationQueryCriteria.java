
package com.zony.app.service.criteria;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.app.domain.User;
import com.zony.app.enums.InstStatusEnum;
import com.zony.app.enums.ProgressStatusEnum;
import lombok.Data;
import com.zony.common.annotation.Query;

import javax.persistence.Convert;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
public class PropagationQueryCriteria implements Serializable {

    @Query
    private Long id;

    @Query(type = Query.Type.INNER_LIKE)
    private String propagationName;

    @Query(type = Query.Type.EQUAL)
    private String username;

    @Query(type = Query.Type.EQUAL)
    private User initUser;

    @Query(type = Query.Type.EQUAL)
    @Convert(converter = ProgressStatusEnum.EnumConverter.class)
    private ProgressStatusEnum status;//状态

    @Query(type = Query.Type.BETWEEN)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private List<Timestamp> launchTime;

    @Query(type = Query.Type.BETWEEN)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private List<Timestamp> implementTime;
}
