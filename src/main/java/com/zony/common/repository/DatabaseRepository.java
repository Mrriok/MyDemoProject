
package com.zony.common.repository;

import com.zony.common.domain.Database;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DatabaseRepository extends JpaRepository<Database, String>, JpaSpecificationExecutor<Database> {
}
