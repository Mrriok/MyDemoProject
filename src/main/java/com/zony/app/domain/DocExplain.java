/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.domain;

import com.zony.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/9/18 -11:32 
 */
@Entity
@Getter
@Setter
@Table(name="app_doc_explain")
public class DocExplain  extends BaseEntity implements Serializable {

    @Id
    @Column(name = "docExplain_id")
    @NotNull(groups = BaseEntity.Update.class)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID", allocationSize = 1, initialValue = 1, sequenceName = "SEQ_ID")
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "释义内容")
    private String value;

    @NotBlank
    @ApiModelProperty(value = "来源：即公司名称")
    private String companyName;

    public DocExplain() {
    }

    public DocExplain(@NotBlank String value, @NotBlank String companyName) {
        this.value = value;
        this.companyName = companyName;
    }
}
