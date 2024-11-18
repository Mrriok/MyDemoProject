
package com.zony.app.repository;

import com.zony.app.domain.AnalysisRights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AnalysisRightsRepository extends JpaRepository<AnalysisRights, Long>, JpaSpecificationExecutor<AnalysisRights> {

}
