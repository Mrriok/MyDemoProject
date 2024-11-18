package com.zony.app.repository;

import com.zony.app.domain.Metadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

public interface MetadataRepository extends JpaRepository<Metadata,Long>,JpaSpecificationExecutor<Metadata>{
    /**
     * 根据Id删除
     *
     * @param ids /
     */
    void deleteAllByIdIn(Set<Long> ids);
}
