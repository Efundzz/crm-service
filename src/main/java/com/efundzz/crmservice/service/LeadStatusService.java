package com.efundzz.crmservice.service;

import com.efundzz.crmservice.DTO.CRMLeadStatusDTO;
import com.efundzz.crmservice.entity.LeadStatus;
import com.efundzz.crmservice.entity.Loan;
import com.efundzz.crmservice.repository.LeadStatusRepository;
import com.efundzz.crmservice.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LeadStatusService {

    @Autowired
    LeadStatusRepository leadStatusRepository;

    @Autowired
    LoanRepository loanRepository;

    public LeadStatus updateLeadStatus(CRMLeadStatusDTO crmLeadStatusDTO) {
        Optional<LeadStatus> optionalLeadStatus = Optional.ofNullable(leadStatusRepository.findByLoanId(crmLeadStatusDTO.getLoanId()));
        if (optionalLeadStatus.isPresent()) {
            LeadStatus leadStatus = optionalLeadStatus.get();
            leadStatus.setLoanId(crmLeadStatusDTO.getLoanId());
            leadStatus.setStatus(crmLeadStatusDTO.getStatus());
            leadStatus.setAgentId(crmLeadStatusDTO.getAgentId());
            leadStatus.setComments(crmLeadStatusDTO.getComments());
            leadStatus.setUpdatedAt(LocalDateTime.now());

            leadStatus = leadStatusRepository.save(leadStatus);
            String loanId = crmLeadStatusDTO.getLoanId();
            Optional<Loan> optionalLoan = Optional.ofNullable(loanRepository.findById(loanId));
            if (optionalLoan.isPresent()) {
                Loan loan = optionalLoan.get();
                loan.setStatus(crmLeadStatusDTO.getStatus());
                loanRepository.save(loan);
            } else {
                throw new EntityNotFoundException("Loan with ID " + loanId + " not found.");
            }
            return leadStatus;
        } else {
            throw new EntityNotFoundException("LeadStatus with ID " + crmLeadStatusDTO.getLoanId() + " not found.");
        }
    }
}
