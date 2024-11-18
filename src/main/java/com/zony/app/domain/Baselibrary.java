package com.zony.app.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.app.enums.*;
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
 * 编制依据库实体类.
 * @author jinweiwei
 * @date 2020/8/4
 */

@Entity
@Getter
@Setter
@Table(name="app_baselibrary")
public class Baselibrary extends BaseEntity implements Serializable{

    @Id
    @Column(name = "baselibrary_id")
    @NotNull(groups = Update.class)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID", allocationSize = 1, initialValue = 1, sequenceName = "SEQ_ID")
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "引用文档")
    private String baselibraryName;

    @CreationTimestamp
    @ApiModelProperty(value = "上传时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp launchTime;

    @NotBlank
    @ApiModelProperty(value = "上传人")
    private String uploaderName;

    @NotNull
    @ApiModelProperty(value = "上传人id")
    private String uploaderId;

    //@ApiModelProperty(value = "制度文档ID: 从那个制度编制文档中添加的，方便流程结束，添加入库，更改状态")
    //private Long systemId;

    @NotNull
    @ApiModelProperty(value = "是否生效标志位")
    private Boolean flag;
}
