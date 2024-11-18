
package com.zony.app.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.app.domain.SystemDocument;
import com.zony.app.domain.User;
import com.zony.app.enums.ProgressStatusEnum;
import com.zony.common.base.BaseDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;


@Data
public class PropagationDetailDto extends BaseDTO implements Serializable {

    private Long id;

    private String propagationName;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp launchTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp implementTime;

    private List<SystemDocument> systemDocumentList;

    //发起部门即为发起人所在部门
    private User initUser;

    private ProgressStatusEnum status;

    private List<User> contactPersonList;

}
