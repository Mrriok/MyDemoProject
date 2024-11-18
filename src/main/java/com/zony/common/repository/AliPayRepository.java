
package com.zony.common.repository;

import com.zony.common.domain.AlipayConfig;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AliPayRepository extends JpaRepository<AlipayConfig,Long> {
}
