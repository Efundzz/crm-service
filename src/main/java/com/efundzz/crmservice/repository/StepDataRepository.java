package com.efundzz.crmservice.repository;

import com.efundzz.crmservice.DTO.CRMAppliacationResponseDTO;
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

    @Query("SELECT new com.efundzz.crmservice.DTO.CRMAppliacationResponseDTO(" +
            "l.id, l.userId, l.status, l.loanType, s.data) " +
            "FROM Loan l " +
            "JOIN StepData s ON l.id = s.applicationId " +
            "WHERE l.id = :appId AND (:brand is null or l.brand = :brand)")
    List<CRMAppliacationResponseDTO> findLeadDataByApplicationId(@Param("appId") String appId, @Param("brand") String brand);

}
