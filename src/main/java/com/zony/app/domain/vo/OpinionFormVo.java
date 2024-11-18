package com.zony.app.domain.vo;

import com.zony.app.domain.Opinion;
import com.zony.app.service.dto.OpinionDto;
import lombok.Data;

/**
 *
 *
 * @Author MrriokChen
 * @Date 2020-08-28
 */
@Data
public class OpinionFormVo extends AttachFormVo {

    private OpinionDto formDtoObj;
    private Opinion formObj;
}
