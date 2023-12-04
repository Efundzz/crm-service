package com.efundzz.crmservice.repository;

import com.efundzz.crmservice.dto.CRMAppliacationResponseDTO;
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

    @Query("SELECT new com.efundzz.crmservice.dto.CRMAppliacationResponseDTO(" +
            "l.id, l.userId, " +
            "COALESCE(ls.status, l.status) AS status, " +
            "l.loanType,l.loanSubType,l.brand,l.createdAt , s.data) " +
            "FROM Loan l " +
            "LEFT JOIN LeadStatus ls ON l.id = ls.loanId " +
            "LEFT JOIN StepData s ON l.id = s.applicationId " +
            "WHERE l.id = :appId AND (:brand is null or l.brand = :brand)")
    List<CRMAppliacationResponseDTO> findLeadDataByApplicationId(@Param("appId") String appId, @Param("brand") String brand);

}
