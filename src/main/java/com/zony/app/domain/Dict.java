
package com.zony.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import com.zony.common.base.BaseEntity;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name="sys_dict")
public class Dict extends BaseEntity implements Serializable {

    @Id
    @Column(name = "dict_id")
    @NotNull(groups = Update.class)
    @ApiModelProperty(value = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID",allocationSize=1,initialValue=1, sequenceName="SEQ_ID")
    private Long id;

    @OneToMany(mappedBy = "dict",fetch = FetchType.LAZY,cascade={CascadeType.PERSIST,CascadeType.REMOVE})
    private List<DictDetail> dictDetails;

    @NotBlank
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;
}
