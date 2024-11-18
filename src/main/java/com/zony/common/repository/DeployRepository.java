
package com.zony.common.repository;

import com.zony.common.domain.Deploy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DeployRepository extends JpaRepository<Deploy, Long>, JpaSpecificationExecutor<Deploy> {
}
