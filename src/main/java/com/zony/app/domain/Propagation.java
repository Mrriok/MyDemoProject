
package com.zony.app.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.app.enums.ProgressStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import com.zony.common.base.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;


@Entity
@Getter
@Setter
@Table(name="app_propagation")
public class Propagation extends BaseEntity implements Serializable {

    @Id
    @Column(name = "propagation_id")
    @NotNull(groups = Update.class)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID", allocationSize = 1, initialValue = 1, sequenceName = "SEQ_ID")
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "宣贯计划名称")
    private String propagationName;

    @ApiModelProperty(value = "实施时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp implementTime;

    @ApiModelProperty(value = "发起时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp launchTime;

    //@OneToOne
    //@JoinColumn(name = "system_document_id")
    //@ApiModelProperty(value = "关联的制度")
    //private SystemDocument systemDocument;
    @ApiModelProperty(value = "宣贯制度ids")
    private String systemDocumentIds;

    //@ApiModelProperty(value = "发起部门编号")
    //private String deptId;
    //
    //@ApiModelProperty(value = "发起部门编号")
    //private String deptName;

    //@ApiModelProperty(value = "发起人账号")
    //private String username;
    //
    //@ApiModelProperty(value = "发起人姓名")
    //private String nickName;

    //发起部门即为发起人所在部门
    @OneToOne
    @JoinColumn(name = "username")
    @ApiModelProperty(value = "发起人")
    private User initUser;

    //@Column(name = "status", length = 200)
    @Convert(converter = ProgressStatusEnum.EnumConverter.class)
    @ApiModelProperty(value = "状态")
    private ProgressStatusEnum status;

    @ApiModelProperty(value = "宣贯对象联系人")
    private String contactPersonIds;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Propagation propagation = (Propagation) o;
        return Objects.equals(id, propagation.id) &&
                Objects.equals(propagationName, propagation.propagationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, propagationName);
    }
}
