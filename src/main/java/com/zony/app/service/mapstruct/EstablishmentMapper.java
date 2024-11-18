/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service.mapstruct;

import com.zony.app.domain.Establishment;
import com.zony.app.service.dto.EstablishmentDto;
import com.zony.common.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 *
 * @author MrriokChen
 * @version v1.0
 * @date 2020/7/30 -16:03
 */
@Mapper(componentModel = "spring", uses = {RegulationMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EstablishmentMapper extends BaseMapper<EstablishmentDto, Establishment> {
}
