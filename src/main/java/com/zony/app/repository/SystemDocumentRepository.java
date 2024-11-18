package com.zony.app.repository;

import com.zony.app.domain.SystemDocument;
import com.zony.app.enums.InstStatusEnum;
import com.zony.app.enums.InstStatusTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;


public interface SystemDocumentRepository extends JpaRepository<SystemDocument, Long>, JpaSpecificationExecutor<SystemDocument> {

    List<SystemDocument> findBySystemCodeOrderByVersion(String systemCode);

    int deleteByIdIn(Set<Long> ids);

    /**
     * 根据制度编号查询所有已入库版本制度信息
     * @Author gubin
     * @Date 2020-09-16
     */
    @Query(value = "from SystemDocument where systemCode=?1 and currentVersionFlag=?2 and instStatusType=?3 and instStatus=?4 order by version desc")
    List<SystemDocument> getAllVersionInLibrary(String systemCode, InstStatusTypeEnum instStatusTypeEnum, InstStatusEnum instStatus);

    /**
     * 根据制度编号和当前版本标记查询制度信息
     * @Author MrriokChen
     * @Date 2020-09-16
     */
    SystemDocument findBySystemCodeAndCurrentVersionFlag(String systemCode, Boolean currentVersionFlag);

}
