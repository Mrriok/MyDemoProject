package com.zony.app.service.criteria;

import com.zony.app.domain.Regulationsort;
import com.zony.app.enums.InstLevelEnum;
import com.zony.app.enums.InstStatusEnum;
import com.zony.app.enums.InstStatusTypeEnum;
import com.zony.common.annotation.Query;
import lombok.Data;
import javax.persistence.Convert;

@Data
public class RegulationlibraryQueryCriteria {

    private Long systemDocumentId;

    private Long regulationsortId;

    @Query(type = Query.Type.RIGHT_LIKE)
    private String systemCode;

    @Query(type = Query.Type.EQUAL)
    private Boolean currentVersionFlag;

    @Query(type = Query.Type.EQUAL)
    @Convert(converter = InstStatusTypeEnum.EnumConverter.class)
    private InstStatusTypeEnum instStatusType;

    @Query(type = Query.Type.EQUAL)
    @Convert(converter = InstStatusEnum.EnumConverter.class)
    private InstStatusEnum instStatus;

    @Query(type = Query.Type.EQUAL)
    @Convert(converter = InstLevelEnum.EnumConverter.class)
    private InstLevelEnum instLevel;

    @Query(type = Query.Type.EQUAL)
    private Regulationsort regulationsort;
}
