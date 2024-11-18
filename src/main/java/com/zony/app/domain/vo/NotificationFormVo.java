package com.zony.app.domain.vo;

import com.zony.app.domain.Notification;
import com.zony.app.domain.SystemDocument;
import com.zony.app.service.dto.NotificationDto;
import com.zony.app.service.dto.SystemDocumentDto;
import lombok.Data;

/**
 * 制度文档表单对象
 *
 * @Author gubin
 * @Date 2020-08-28
 */
@Data
public class NotificationFormVo extends AttachFormVo {

    private Notification formObj;//表单实体类对象
    private NotificationDto formDtoObj;//表单元数据对象
}
