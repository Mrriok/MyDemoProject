
package com.zony.app.repository;

import com.zony.app.domain.AnalysisTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AnalysisTableRepository extends JpaRepository<AnalysisTable, Long>, JpaSpecificationExecutor<AnalysisTable> {

}
