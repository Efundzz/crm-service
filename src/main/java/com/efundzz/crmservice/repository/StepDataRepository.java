package com.efundzz.crmservice.repository;

import com.efundzz.crmservice.entity.StepData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StepDataRepository extends JpaRepository<StepData, Long> {
    List<StepData> findByApplicationId(String id);

    StepData findTopByApplicationIdAndStepName(String id, String stepName);

    @Query("SELECT s FROM StepData s WHERE s.stepName IN (:stepNames) AND s.applicationId = :id")
    List<StepData> getStepsByStepName(@Param("id") String id, @Param("stepNames") List<String> stepNames);

    @Query("SELECT s FROM StepData s WHERE s.applicationId = :appId")
    List<StepData> findLeadDataByApplicationId(@Param("appId") String appId);
}
