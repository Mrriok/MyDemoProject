package com.zony.app.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.app.domain.Regulationsort;
import com.zony.app.enums.*;
import com.zony.common.base.BaseDTO;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
public class SystemDocumentDto extends BaseDTO implements Serializable {

    private Long id;

    private RegulationDto regulation;//制度制修订计划

    private String systemTitle;//制度名称

    private String initDept;

    private String initUser;

    private InstLevelEnum instLevel;

    private String instLevelName;

    private InstPropertyEnum instProperty;

    private String instPropertyName;

    private Regulationsort regulationsort;

    private String institutionalName;//体系名称

    private String systemCode;//制度编码

    private String companyName;//公司名称

    private String approver;//批准人

    private String according;//批准依据

    private String draftDept;//牵头起草部门

    private String publishRange;//发布范围

    private String version;//版本

    private String reference;

    private String abolish;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp dateOfWriting;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp publishDate;//发布日期

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Timestamp effectiveDate;//生效日期

    private List<String> docPurpose;//目的

    //private List<String> docPurposeList;

    private List<String> docApplyRange;//适用范围

    //private List<String> docApplyRangeList;

    private List<SystemDocumentSmallDto> docAccording;//编制依据

    //private List<String> docAccordingList;

    private List<DocReplyRiskTargetDto> docReplyRiskTarget;//主要应对的风险与目标

    //private List<String> docReplyRiskTargetList;

    //private String docReplyTarget;//主要应对的目标

    private List<DocExplainDto> docExplain;//释义

    //private List<String> docExplainList;

    private String publishSymbol;

    private InstStatusEnum instStatus;

    private InstStatusTypeEnum instStatusType;

    private Boolean currentVersionFlag;

    private String modifyCode;

    private Long deptId;
}
