package com.zony.app.repository;

import com.zony.app.domain.Dept;
import com.zony.app.domain.Regulation;
import com.zony.app.domain.Regulationsort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface RegulationsortRepository extends JpaRepository<Regulationsort, Long>, JpaSpecificationExecutor<Regulationsort> {

    /**
     * 判断是否存在子节点
     *
     * @param pid /
     * @return /
     */
    int countByPid(Long pid);

    /**
     * 根据ID更新sub_count
     *
     * @param count /
     * @param id    /
     */
    @Modifying
    @Query(value = " update test_regulationsort set sub_count = ?1 where regulationsort_id = ?2 ", nativeQuery = true)
    void updateSubCntById(Integer count, Long id);

    /**
     * 根据 PID 查询
     *
     * @param id pid
     * @return /
     */
    List<Regulationsort> findByPid(Long id);

    /**
     * 获取顶级部门
     *
     * @return /
     */
    List<Regulationsort> findByPidIsNull();

    /**
     * 根据Id删除
     *
     * @param ids /
     */
    void deleteAllByIdIn(Set<Long> ids);

    Regulationsort findByMenuId(String menuId);

    Regulationsort findByMenuIdAndPid(String menuId, Long id);
}
