/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.repository;

import com.zony.app.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
 * 从数据库中按照规则查询公告数据.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/8 -15:38 
 */
public interface NoticeRepository extends JpaRepository<Notice,Long> ,JpaSpecificationExecutor<Notice>{
    /**
     * 根据公共名称查询
     * @param title /
     * @return /
     */
    List<Notice> findByTitle(String title);

    /**
     * 根据公共名称查询
     * @param effectiveTime /
     * @return /
     */
    List<Notice> findByEffectiveTime(Timestamp effectiveTime);

    /**
     * 删除多个公告
     * @param ids /
     */
    void deleteAllByIdIn(Set<Long> ids);

    /**
     * 获取公告数量
     * @param id /
     * @return /
     */
    int countById(Long id);

    /**
     * 根据公告题目查询
     *
     * @param title /
     * @return /
     */
    @Query(value = "SELECT count(1) FROM Test_ChenHang_Notice tcn WHERE tcn.title IN ?1", nativeQuery = true)
    int countByTitle(String title);

    /**
     * 根据公告有效时间查询
     *
     * @param title /
     * @return /
     */
    @Query(value = "SELECT count(1) FROM Test_ChenHang_Notice tcn WHERE tcn.effective_time = ?1", nativeQuery = true)
    int countByEffectiveTime(String title);


}
