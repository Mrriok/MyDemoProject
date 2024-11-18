
package com.zony.app.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.zony.common.base.BaseDTO;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
public class JobDto extends BaseDTO implements Serializable {

    private Long id;

    private Integer jobSort;

    private String name;

    private Boolean enabled;

    public JobDto(String name, Boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }
}