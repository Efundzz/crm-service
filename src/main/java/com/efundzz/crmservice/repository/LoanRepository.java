package com.efundzz.crmservice.repository;

import com.efundzz.crmservice.DTO.CRMAppliacationResponseDTO;
import com.efundzz.crmservice.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Serializable> {

    Loan findById(String id);

@Query("SELECT new com.efundzz.crmservice.DTO.CRMAppliacationResponseDTO(" +
        "l.id, l.userId, " +
        "COALESCE(ls.status, l.status) AS status, " +
        "l.loanType, l.loanSubType,s.data) " +
        "FROM Loan l " +
        "LEFT JOIN LeadStatus ls ON l.id = ls.loanId " +
        "LEFT JOIN StepData s ON l.id = s.applicationId " +
        "WHERE (:brand is null or l.brand = :brand)")
    List<CRMAppliacationResponseDTO> findAllWithStepData(@Param("brand") String brand);

    @Query("SELECT new com.efundzz.crmservice.DTO.CRMAppliacationResponseDTO(" +
            "l.id, l.userId, l.status, l.loanType,l.loanSubType, s.data) " +
            "FROM Loan l " +
            "JOIN StepData s ON l.id = s.applicationId " +
            "WHERE (:brand is null or l.brand = :brand) " +
            "AND (:loanType is null or l.loanType = :loanType) " +
            "AND (:fromDate is null OR l.createdAt >= TO_TIMESTAMP(:fromDate, 'YYYY-MM-DD')) " +
            "AND (:toDate is null OR l.createdAt <= TO_TIMESTAMP(:toDate, 'YYYY-MM-DD')) " +
            "AND (:todayDate is null OR DATE(l.createdAt) = TO_TIMESTAMP(:todayDate, 'YYYY-MM-DD')) " +
            "AND (:loanStatus is null or l.status = :loanStatus)")
    List<CRMAppliacationResponseDTO> findAllStepDataByCriteria(
            @Param("brand") String brand,
            @Param("loanType") String loanType,
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate,
            @Param("todayDate") String todayDate,
            @Param("loanStatus") String loanStatus);

}
