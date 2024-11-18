package com.zony.app.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.common.annotation.Query;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class BaselibraryDto {

    private Long id;

    private String baselibraryName;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp launchTime;

    private String uploaderName;

    private String uploaderId;

    //private Long systemId;

    private Boolean flag;
}
