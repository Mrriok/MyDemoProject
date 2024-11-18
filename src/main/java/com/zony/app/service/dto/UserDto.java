
package com.zony.app.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import com.zony.common.base.BaseDTO;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;


@Getter
@Setter
public class UserDto extends BaseDTO implements Serializable {

    private Long id;

    private Long userId;

    private Set<RoleSmallDto> roles;

    private Set<JobSmallDto> jobs;

    private DeptSmallDto dept;

    private Long deptId;

    private String username;

    private String nickName;

    private String email;

    private String phone;

    private String gender;

    private String avatarName;

    private String avatarPath;

    @JsonIgnore
    private String password;

    private Boolean enabled;

    @JsonIgnore
    private Boolean isAdmin = false;


    private Boolean isContact;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp pwdResetTime;
}
