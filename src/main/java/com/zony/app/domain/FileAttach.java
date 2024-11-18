package com.zony.app.domain;

import com.zony.app.enums.SystemDocAttachEnum;
import com.zony.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "app_file_attach")
public class FileAttach extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 5604731867701307347L;
    @Id
    @Column(name = "file_attach_id")
    @NotNull(groups = Update.class)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID", allocationSize = 1, initialValue = 1, sequenceName = "SEQ_ID")
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;
    @Column(name = "attach_title")
    @ApiModelProperty(value = "原文件名")
    private String attachTitle;
    @Column(name = "attach_type")
    @ApiModelProperty(value = "文件类型")
    private String attachType;
    @Column(name = "attach_size")
    @ApiModelProperty(value = "文件大小")
    private Long attachSize;
    @Column(name = "seq_num")
    @ApiModelProperty(value = "排列序号")
    private Integer seqNum;
    @Column(name = "fn_id")
    @ApiModelProperty(value = "FileNet文档id")
    private String fnId;
    @Column(name = "main_table")
    @ApiModelProperty(value = "所属主体表")
    private String mainTable;
    @Column(name = "main_id")
    @ApiModelProperty(value = "所属主体id")
    private Long mainId;
    @Column(name = "md5")
    @ApiModelProperty(value = "md5校验码")
    private String md5;
    @Column(name = "path")
    @ApiModelProperty(value = "附件保存路径")
    private String path;
    @Column(name = "system_doc_type")
    @ApiModelProperty(value = "制度附件类型")
    @Convert(converter = SystemDocAttachEnum.EnumConverter.class)
    private SystemDocAttachEnum systemDocType;
}
