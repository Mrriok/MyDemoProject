/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 填写功能简述.
 * <p>公告实体类.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/8 -15:04
 */
@Entity
@Getter
@Setter
@Table(name="test_chenhang_notice")
public class Notice extends BaseEntity implements Serializable {

    @Id
    @Column(name = "notice_id")
    @NotNull(groups = {BaseEntity.Update.class})
    @ApiModelProperty(value = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID",allocationSize=1,initialValue=1, sequenceName="SEQ_ID")
    private Long id;


    @NotBlank
    @ApiModelProperty(value = "公告题目")
    private String title;

    @NotNull
    @ApiModelProperty(value = "有效时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp effectiveTime;

    @NotNull
    @ApiModelProperty(value = "公共内容")
    private String content;


    @ApiModelProperty(value = "备注")
    private String mark;

    @ApiModelProperty(value = "附件信息唯一标识id")
    private String documentId;

}
