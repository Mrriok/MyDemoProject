
package com.zony.common.domain;

import com.zony.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Setter
@Entity
@Table(name="tool_local_storage")
@NoArgsConstructor
public class LocalStorage extends BaseEntity implements Serializable {

    @Id
    @Column(name = "storage_id")
    @ApiModelProperty(value = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID",allocationSize=1,initialValue=1, sequenceName="SEQ_ID")
    private Long id;

    @ApiModelProperty(value = "真实文件名")
    private String realName;

    @ApiModelProperty(value = "文件名")
    private String name;

    @ApiModelProperty(value = "后缀")
    private String suffix;

    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "类型")
    private String type;

    @Column(name = "storage_size")
    @ApiModelProperty(value = "大小")
    private String size;

    public LocalStorage(String realName,String name, String suffix, String path, String type, String size) {
        this.realName = realName;
        this.name = name;
        this.suffix = suffix;
        this.path = path;
        this.type = type;
        this.size = size;
    }

    public void copy(LocalStorage source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}