/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.repository;

import com.zony.app.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/13 -9:45 
 */
public interface NotificationRepository extends JpaRepository<Notification,Long>, JpaSpecificationExecutor<Notification> {

    /**
     * 统计收到的未读消息条数
     *
     * @param username /
     * @return /
     */
    @Query(value = "select count(1) from APP_NOTIFICATION t where t.read_flag = 0 and t.to_someone like ?1 and t.is_delete = 0", nativeQuery = true)
    int countByToSomeoneAndReadFlag(String username);

    /**
     * 删除多条消息
     * @param ids /
     */
    void deleteAllByIdIn(Set<Long> ids);

    /**
     * 根据id查询消息
     *
     * @param ids /
     * @return /
     */
    List<Notification> findAllByIdIn(Set<Long> ids);
}
