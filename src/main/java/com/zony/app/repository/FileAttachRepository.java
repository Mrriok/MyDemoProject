
package com.zony.app.repository;

import com.zony.app.domain.FileAttach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface FileAttachRepository extends JpaRepository<FileAttach, Long>, JpaSpecificationExecutor<FileAttach> {

    List<FileAttach> findByMainIdAndMainTable(Long mainId, String mainTable);

    FileAttach findOneByFnId(String fnId);
}
