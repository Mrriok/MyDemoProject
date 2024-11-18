/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.app.enums.InstLevelEnum;
import com.zony.app.enums.InstPropertyEnum;
import com.zony.app.enums.ProgressStatusEnum;
import com.zony.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 征求意见实体类.
 * <p>与数据库表结构对应.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/14 -17:46
 */
@Entity
@Getter
@Setter
@Table(name="app_opinion")
public class Opinion extends BaseEntity implements Serializable {

    @Id
    @Column(name = "opinion_id")
    @NotNull(groups = Update.class)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID", allocationSize = 1, initialValue = 1, sequenceName = "SEQ_ID")
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "意见征求名称")
    private String opinionName;

    @NotBlank
    @ApiModelProperty(value = "牵头起草部门")
    private String draftDept;

    @NotBlank
    @ApiModelProperty(value = "制度编码")
    private String instCode;

    @OneToOne
    @JoinColumn(name = "username")  //发起部门在发起人信息中
    @ApiModelProperty(value = "发起人")
    private User initUser;

    @NotBlank
    @ApiModelProperty(value = "制度名称")
    private String instName;

    @NotNull
    @Convert(converter = InstPropertyEnum.EnumConverter.class)
    @ApiModelProperty(value = "制度属性")
    private InstPropertyEnum instProperty;

    @NotNull
    @Convert(converter = InstLevelEnum.EnumConverter.class)
    @ApiModelProperty(value = "制度层级")
    private InstLevelEnum instLevel;

    @NotNull
    @Column(name = "deadline")
    @ApiModelProperty(value = "截止时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp deadline;

    //jsonString
    @ApiModelProperty(value = "征求部门联系人ids")
    private String contactPersonIds;

    @Convert(converter = ProgressStatusEnum.EnumConverter.class)
    @ApiModelProperty(value = "征求意见状态")
    private ProgressStatusEnum status;

    //@Column(name = "deptId")//对象 systemTitle,systemCode,jsonString
    //@ApiModelProperty(value = "制度所属部门id")
    //private Long deptId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Opinion opinion = (Opinion) o;
        return Objects.equals(id, opinion.id) &&
                Objects.equals(opinionName, opinion.opinionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, opinionName);
    }
}
