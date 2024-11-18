package com.zony.app.domain.vo;

import com.zony.app.domain.FileAttach;
import com.zony.app.domain.SystemDocument;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 制度库—制度文档查看详情视图类
 */
@Data
public class SystemDocumentDetailVo implements Serializable {

    private SystemDocument systemDocument;//基本信息
    private List<FileAttach> attachList;//附件集合
}
