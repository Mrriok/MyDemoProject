package com.zony.app.service.dto;

import com.zony.common.base.BaseDTO;
import lombok.Data;

import java.io.Serializable;

@Data
public class CollectDto extends BaseDTO implements Serializable {

    private Long id;

    private Long systemDocumentId;

    private String systemCode;

    private String systemTitle;

    private String initDept;

    private String initUser;

    private String username;
}
