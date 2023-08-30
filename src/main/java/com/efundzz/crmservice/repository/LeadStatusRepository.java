package com.efundzz.crmservice.repository;

import com.efundzz.crmservice.entity.LeadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface LeadStatusRepository extends JpaRepository<LeadStatus, Serializable> {
    LeadStatus findByLoanId(String loanId);

    LeadStatus save(LeadStatus leadStatus);
}
