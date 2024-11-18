
package com.zony.app.repository;

import com.zony.app.domain.AnalysisItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface AnalysisItemRepository extends JpaRepository<AnalysisItem, Long>, JpaSpecificationExecutor<AnalysisItem> {

}
