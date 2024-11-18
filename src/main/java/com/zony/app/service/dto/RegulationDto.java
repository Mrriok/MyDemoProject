package com.zony.app.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.app.domain.Regulationsort;
import com.zony.app.domain.SystemDocument;
import com.zony.app.domain.User;
import com.zony.app.enums.*;
import com.zony.common.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Data
public class RegulationDto extends BaseDTO implements Serializable {

    private Long id;

    private User initUser;

    private String instName;

    private String instCode;

    @Convert(converter = InstLevelEnum.EnumConverter.class)
    private InstLevelEnum instLevel;

    @Convert(converter = InstPropertyEnum.EnumConverter.class)
    private InstPropertyEnum instProperty;

    //private String reference;
    //private String docAccording;

    private String publishRange;

    private String companyName;

    private String abolish;

    //private List<SystemDocument> referenceList;
    private String docAccording;

    private List<Map<String,Object>> docAccordingList;

    private List<SystemDocument> abolishList;

    private String reason;

    private String delayReason;

    private ProgressStatusEnum status;

    private Regulationsort regulationsort;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp expectedTime;

    private Boolean isSubmit;

    private Long deptId;
}
