package com.zony.app.repository;

import com.zony.app.domain.Baselibrary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
public interface BaselibraryRepository extends JpaRepository<Baselibrary,Long>,JpaSpecificationExecutor<Baselibrary>{

    /**
     * 根据Id删除
     *
     * @param ids /
     */
    void deleteAllByIdIn(Set<Long> ids);

    Baselibrary findByBaselibraryName(String baselibraryName);
}
