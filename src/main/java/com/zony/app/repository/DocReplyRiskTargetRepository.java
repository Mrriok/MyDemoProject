/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.repository;

import com.zony.app.domain.DictDetail;
import com.zony.app.domain.DocReplyRiskTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/9/18 -13:56 
 */
public interface DocReplyRiskTargetRepository  extends JpaRepository<DocReplyRiskTarget, Long>, JpaSpecificationExecutor<DocReplyRiskTarget> {

    int deleteByIdIn(Set<Long> ids);

    DocReplyRiskTarget findByValueAndCompanyName(String value,String companyName);

}
