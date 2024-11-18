
package com.zony.app.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.zony.common.base.BaseDTO;

import java.io.Serializable;
import java.sql.Timestamp;


@Data
public class HelpDto extends BaseDTO implements Serializable {
    private Long id;

    private String name;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp updateTime;

    private String instruction;

    private String attachments;

    private String type;

    private UserDto author;
}
