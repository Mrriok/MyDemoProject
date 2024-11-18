
package com.zony.common.repository;

import com.zony.common.domain.ServerDeploy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ServerDeployRepository extends JpaRepository<ServerDeploy, Long>, JpaSpecificationExecutor<ServerDeploy> {

    /**
     * 根据IP查询
     * @param ip /
     * @return /
     */
    ServerDeploy findByIp(String ip);
}
