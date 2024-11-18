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
 * 制度收藏实体类.
 * @author jinweiwei
 * @date 2020/8/5
 */

@Entity
@Getter
@Setter
@Table(name="app_collect")
public class Collect extends BaseEntity implements Serializable{

    @Id
    @Column(name = "collect_id")
    @NotNull(groups = Update.class)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID", allocationSize = 1, initialValue = 1, sequenceName = "SEQ_ID")
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "收藏制度id")
    private Long systemDocumentId;

    @NotBlank
    @ApiModelProperty(value = "收藏制度编码")
    private String systemCode;

    @NotBlank
    @ApiModelProperty(value = "收藏制度标题")
    private String systemTitle;

    @NotBlank
    @ApiModelProperty(value = "收藏制度编写部门")
    private String initDept;

    @NotBlank
    @ApiModelProperty(value = "收藏制度编写人")
    private String initUser;

    @NotBlank
    @ApiModelProperty(value = "收藏人username")
    private String username;

}
