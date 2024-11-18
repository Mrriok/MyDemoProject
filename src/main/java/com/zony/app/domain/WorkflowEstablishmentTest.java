/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.sql.Update;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/8/28 -18:22 
 */
@Entity
@Getter
@Setter
@Table(name = "app_workflow_Establishment_test")
public class WorkflowEstablishmentTest {
    @Id
    @Column(name = "workflow_establishment_id")
    @NotNull(groups = Update.class)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID", allocationSize = 1, initialValue = 1, sequenceName = "SEQ_ID")
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @OneToOne
    @JoinColumn(name = "workflow_master_id")
    @ApiModelProperty(value = "所属流程", hidden = true)
    private WorkflowMaster workflowMaster;

    @OneToOne
    @JoinColumn(name = "establishment_id")
    @ApiModelProperty(value = "关联的制度编制", hidden = true)
    private Establishment establishment;
}
