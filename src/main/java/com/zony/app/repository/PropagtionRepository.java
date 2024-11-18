
package com.zony.app.repository;

import com.zony.app.domain.Propagation;
import com.zony.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.Date;
import java.util.List;
import java.util.Set;


public interface PropagtionRepository extends JpaRepository<Propagation, Long>, JpaSpecificationExecutor<Propagation> {


  /**
   * 根据Id删除
   * 
   * @param ids /
   */
  void deleteAllByIdIn(Set<Long> ids);


}
