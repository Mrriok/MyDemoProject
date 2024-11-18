package com.zony.app.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.List;

@Data
public class RegulationsortDto {
    private Long id;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<RegulationsortDto> children;

    private String name;

    private Long pid;

    private Integer subCount;

    private Boolean enabled;

    private String menuId;

    public Boolean getHasChildren() {
        return subCount > 0;
    }

    public Boolean getLeaf() {
        return subCount <= 0;
    }

    public String getLabel() {
        return name;
    }

}
