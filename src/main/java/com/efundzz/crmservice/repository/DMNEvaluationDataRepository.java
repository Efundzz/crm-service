package com.efundzz.crmservice.repository;

import com.efundzz.crmservice.entity.DMNEvaluationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DMNEvaluationDataRepository extends JpaRepository<DMNEvaluationData, Long> {

}
