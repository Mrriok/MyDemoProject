/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.domain;

import java.sql.Timestamp;
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
 * @date 2020/7/13 -9:22 
 */
@Entity
@Getter
@Setter
@Table(name="app_notification")
public class Notification extends BaseEntity implements Serializable {
    @Id
    @Column(name = "notification_id")
    @NotNull(groups = Update.class)
    @ApiModelProperty(value = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID",allocationSize=1,initialValue=1, sequenceName="SEQ_ID")
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "消息题目")
    private String messageTitle;

    @NotBlank
    @ApiModelProperty(value = "消息内容")
    private String messageContent;

    @NotBlank //消息接收者 接收者可以为多人 ,发送者为创建人
    @ApiModelProperty(value = "消息接收者")
    private String toSomeone;

    @ApiModelProperty(value = "备注")
    private String remark;

    //@NotNull
    @ApiModelProperty(value = "发送时间")
    private Timestamp sentTime;


    @ApiModelProperty(value = "是否已读标志,false:未读，true：已读 , null:草稿/未发生")
    private Boolean readFlag;

    @NotNull
    @ApiModelProperty(value = "删除标志，false：未删除，true：删除")
    private Boolean isDelete;
}
