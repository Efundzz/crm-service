package com.efundzz.crmservice.service;

import com.efundzz.crmservice.DTO.CRMLeadStatusDTO;
import com.efundzz.crmservice.entity.LeadStatus;
import com.efundzz.crmservice.repository.LeadStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LeadStatusService {

    @Autowired
    LeadStatusRepository leadStatusRepository;

    public LeadStatus updateLeadStatus(CRMLeadStatusDTO crmLeadStatusDTO) {
        Optional<LeadStatus> optionalLeadStatus = Optional.ofNullable(leadStatusRepository.findByLoanId(crmLeadStatusDTO.getLoanId()));
        if (optionalLeadStatus.isPresent()) {
            LeadStatus leadStatus = optionalLeadStatus.get();
            leadStatus.setLoanId(crmLeadStatusDTO.getLoanId());
            leadStatus.setStatus(crmLeadStatusDTO.getStatus());
            leadStatus.setAgentId(crmLeadStatusDTO.getAgentId());
            leadStatus.setComments(crmLeadStatusDTO.getComments());
            leadStatus.setUpdatedAt(LocalDateTime.now());
            return leadStatusRepository.save(leadStatus);
        } else {
            throw new EntityNotFoundException("LeadStatus with ID " + crmLeadStatusDTO.getLoanId() + " not found.");
        }
    }
}
