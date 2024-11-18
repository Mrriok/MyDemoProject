
package com.zony.app.service.dto;

import lombok.Getter;
import lombok.Setter;
import com.zony.common.base.BaseDTO;
import java.io.Serializable;


@Getter
@Setter
public class DictDetailDto extends BaseDTO implements Serializable {

    private Long id;

    private DictSmallDto dict;

    private String label;

    private String value;

    private Integer dictSort;
}