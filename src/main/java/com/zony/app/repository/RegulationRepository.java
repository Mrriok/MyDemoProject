package com.zony.app.repository;

import com.zony.app.domain.Regulation;
import com.zony.app.enums.ProgressStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

public interface RegulationRepository extends JpaRepository<Regulation,Long> , JpaSpecificationExecutor<Regulation> {

    /**
     * 根据Id删除
     *
     * @param ids /
     */
    void deleteAllByIdIn(Set<Long> ids);

    List<Regulation> findAllByIdIn(Set<Long> ids);

    List<Regulation> findAllByDeptIdAndStatusIn(Long deptId,List<ProgressStatusEnum> statusList);

    @Query(value = "SELECT r.* FROM app_regulation r WHERE status != ?3 " +
            "AND expected_time BETWEEN ?1 AND ?2  ORDER BY create_time DESC ",nativeQuery = true )
    List<Regulation> findAllByExpectedTimeAndStatus(Timestamp startTime, Timestamp endTime,ProgressStatusEnum status);
}

