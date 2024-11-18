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
import com.zony.app.enums.WorkflowClassEnum;
import com.zony.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 填写功能简述.
 * <p>公告实体类.<br>
 * @version v1.0
 * @author Zhijie Yu
 * @date 2020/7/28 -15:04
 */
@Entity
@Getter
@Setter
@Table(name="app_help")
public class Help extends BaseEntity implements Serializable {

    @Id
    @Column(name = "help_id")
    @NotNull(groups = {BaseEntity.Update.class})
    @ApiModelProperty(value = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID",allocationSize=1,initialValue=1, sequenceName="SEQ_ID")
    private Long id;


    @NotBlank
    @ApiModelProperty(value = "文件名称")
    private String name;

    @CreationTimestamp
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp updateTime;

    @ApiModelProperty(value = "说明")
    private String Instruction;

    @ApiModelProperty(value = "帮助材料")
    private String attachment;

    //@Column(name = "status", length = 200)
    @NotNull
    @ApiModelProperty(value = "征求意见状态")
    private String type;

    @OneToOne
    @JoinColumn(name = "username")
    @ApiModelProperty(value = "上传者")
    private User author;



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Help help = (Help) o;
        return Objects.equals(id, help.id) &&
                Objects.equals(name, help.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
