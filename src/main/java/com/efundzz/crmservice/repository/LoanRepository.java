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

    @Query("SELECT new com.efundzz.crmservice.DTO.CRMAppliacationResponseDTO(" +
            "l.id, l.userId, l.status, l.loanType, s.data) " +
            "FROM Loan l " +
            "JOIN StepData s ON l.id = s.applicationId WHERE (:brand is null or l.brand = :brand)")
    List<CRMAppliacationResponseDTO> findAllWithStepData(@Param("brand") String brand);

}
