
package com.zony.common.repository;

import com.zony.common.domain.DeployHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DeployHistoryRepository extends JpaRepository<DeployHistory, String>, JpaSpecificationExecutor<DeployHistory> {
}
