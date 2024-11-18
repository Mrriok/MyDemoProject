package com.zony.app.service.dto;

import lombok.Data;

import java.util.List;

/**
 * 多层制度信息，制度库图形展示使用
 */
@Data
public class SystemDocSubListDto {

    private Long id;
    private String name;
    private String code;
    private List<SystemDocSubListDto> children;
}
