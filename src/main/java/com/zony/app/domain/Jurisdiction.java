package com.zony.app.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 权限手册实体类
 *
 * @Author gubin
 * @Date 2020-09-22
 */

@Entity
@Getter
@Setter
@Table(name = "app_jurisdiction")
public class Jurisdiction extends BaseEntity implements Serializable {

    @Id
    @Column(name = "jurisdiction_id")
    @NotNull(groups = Update.class)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID", allocationSize = 1, initialValue = 1, sequenceName = "SEQ_ID")
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "权限手册名称")
    private String jurisdictionName;

    @OneToOne
    @JoinColumn(name = "username")
    @ApiModelProperty(value = "提交人")
    private User updatePerson;

    @CreationTimestamp
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp launchTime;

    @NotBlank
    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "版本号")
    private String sequence;

    @OneToOne
    @JoinColumn(name = "file_attach_id")
    @ApiModelProperty(value = "附件信息")
    private FileAttach fileAttach;

    @OneToMany(mappedBy = "jurisdiction", fetch = FetchType.LAZY)
    @ApiModelProperty(value = "权限手册表格", hidden = true)
    private Set<AnalysisTable> analysisTableSet;

}
