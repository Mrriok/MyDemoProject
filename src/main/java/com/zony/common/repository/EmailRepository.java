
package com.zony.common.repository;

import com.zony.common.domain.EmailConfig;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmailRepository extends JpaRepository<EmailConfig,Long> {
}
