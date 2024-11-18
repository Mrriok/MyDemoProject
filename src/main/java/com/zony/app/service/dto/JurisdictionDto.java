package com.zony.app.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class JurisdictionDto {

    private Long id;

    private String jurisdictionName;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp launchTime;

    private UserDto updatePerson;

    private String companyName;

    private String sequence;

    private FileAttachDto fileAttach;

}
