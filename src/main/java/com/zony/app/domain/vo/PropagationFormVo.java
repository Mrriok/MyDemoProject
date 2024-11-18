package com.zony.app.domain.vo;

import com.zony.app.domain.Propagation;
import com.zony.app.service.dto.PropagationDetailDto;
import lombok.Data;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/9/22 -18:26
 */
@Data
public class PropagationFormVo extends AttachFormVo {
    private PropagationDetailDto formDtoObj;//表单元数据对象
    private Propagation formObj;//表单元数据对象
}
