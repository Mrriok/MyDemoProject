package com.zony.app.repository;

import com.zony.app.domain.Baselibrary;
import com.zony.app.domain.Collect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

public interface CollectRepository extends JpaRepository<Collect,Long>,JpaSpecificationExecutor<Collect>{

    /**
     * 根据Id删除
     *
     * @param ids /
     */
    void deleteAllByIdIn(Set<Long> ids);
}
