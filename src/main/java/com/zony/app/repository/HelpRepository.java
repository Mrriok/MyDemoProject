package com.zony.app.repository;

import com.zony.app.domain.Help;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

public interface HelpRepository extends JpaRepository<Help, Long>, JpaSpecificationExecutor<Help> {
    /**
     * 根据名称查询
     * @param name 名称
     * @return /
     */
    Help findByName(String name);



    /**
     * 根据Id删除
     * @param ids /
     */
    void deleteAllByIdIn(Set<Long> ids);
}
