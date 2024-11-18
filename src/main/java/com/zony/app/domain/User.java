
package com.zony.app.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import com.zony.common.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;


@Entity
@Getter
@Setter
@Table(name = "sys_user")
public class User extends BaseEntity implements Serializable {

    @Id
    @NotBlank
    @Column
    @ApiModelProperty(value = "用户账号")
    private String username;

    @Column(name = "user_id")
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @ApiModelProperty(value = "用户角色")
    @JoinTable(name = "sys_users_roles",
            joinColumns = {@JoinColumn(name = "username", referencedColumnName = "username")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "role_id")})
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY)
    @ApiModelProperty(value = "用户岗位")
    @JoinTable(name = "sys_users_jobs",
            joinColumns = {@JoinColumn(name = "username", referencedColumnName = "username")},
            inverseJoinColumns = {@JoinColumn(name = "job_id", referencedColumnName = "job_id")})
    private Set<Job> jobs;

    @OneToOne
    @JoinColumn(name = "dept_id")
    @ApiModelProperty(value = "用户部门")
    private Dept dept;

    @NotBlank
    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @Email
    @NotBlank
    @ApiModelProperty(value = "邮箱")
    private String email;

    @NotBlank
    @ApiModelProperty(value = "电话号码")
    private String phone;

    @ApiModelProperty(value = "用户性别")
    private String gender;

    @ApiModelProperty(value = "头像真实名称", hidden = true)
    private String avatarName;

    @ApiModelProperty(value = "头像存储的路径", hidden = true)
    private String avatarPath;

    @ApiModelProperty(value = "密码")
    private String password;

    @NotNull
    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @ApiModelProperty(value = "是否为admin账号", hidden = true)
    private Boolean isAdmin = false;


    @ApiModelProperty(value = "是否为部门联系人")
    private Boolean isContact;

    @Column(name = "pwd_reset_time")
    @ApiModelProperty(value = "最后修改密码的时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp pwdResetTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return
                Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Transient
    public Long getUserId() {
        return id;
    }

    public void setUserId(Long id) {
        this.setId(id);
    }
}
