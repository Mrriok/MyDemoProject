
package com.zony.common.repository;

import com.zony.common.domain.QuartzLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface QuartzLogRepository extends JpaRepository<QuartzLog,Long>, JpaSpecificationExecutor<QuartzLog> {

}
