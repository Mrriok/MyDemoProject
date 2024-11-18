
package com.zony.app.repository;

import com.zony.app.domain.AnalysisGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface AnalysisGroupRepository extends JpaRepository<AnalysisGroup, Long>, JpaSpecificationExecutor<AnalysisGroup> {

}
