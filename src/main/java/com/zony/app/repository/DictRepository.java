
package com.zony.app.repository;

import com.zony.app.domain.Dict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Set;


public interface DictRepository extends JpaRepository<Dict, Long>, JpaSpecificationExecutor<Dict> {

    /**
     * 删除
     * @param ids /
     */
    void deleteByIdIn(Set<Long> ids);

    /**
     * 查询
     * @param ids /
     * @return /
     */
    List<Dict> findByIdIn(Set<Long> ids);

    /**
     * 查询字典主体（TODO：有个bug，名称标识可重复创建，时间紧急先这样，后续请更改逻辑）
     * @param name 字典英文名称
     * @return Dict
     */
    Dict findByName(String name);
}