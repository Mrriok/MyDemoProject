package com.zony.app.repository;

import com.zony.app.domain.Jurisdiction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JurisdictionReposity extends JpaRepository<Jurisdiction,Long>,JpaSpecificationExecutor<Jurisdiction>{

    int countByCompanyName(String companyName);
}
