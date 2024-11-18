package com.zony.app.service.dto;

import com.zony.app.enums.InstLevelEnum;
import lombok.Data;
import java.util.List;

/**
 * 读写word制度文档
 *
 * @Author gubin
 * @Date 2020-09-17
 */
@Data
public class SystemDocumentWordDto {

    private InstLevelEnum instLevel;//制度层级

    private String systemTitle;//制度名称
    private String systemCode;//编码
    private String version;//版本号
    private String modifyCode;//修改码
    private String header;//页眉：体系名称/-/编码 公司名称/制度名称

    private String companyName;//公司名称
    private String approver;//批准人
    private String according;//批准依据
    private String publishRange;//发布范围
    private String publishSymbol;//发布文号
    private String publishDateStr;//发布日期
    private String effectiveDateStr;//生效日期
    private String institutionalName;//体系名称

    private List<String> docPurpose;//1 目的
    private List<String> docApplyRange;//2 适用范围
    private List<SystemDocumentSmallDto> docAccording;//3 编制依据
    private List<DocReplyRiskTargetDto> docReplyRiskTarget;//4 主要应对的风险及应对目标
    private List<DocExplainDto> docExplain;//5 释义
}
