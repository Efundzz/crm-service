package com.efundzz.crmservice.service;

import com.efundzz.crmservice.dto.CRMLeadFormUpdateDTO;
import com.efundzz.crmservice.entity.Leads;
import com.efundzz.crmservice.entity.LeadsLog;
import com.efundzz.crmservice.repository.LeadRepository;
import com.efundzz.crmservice.repository.LeadsLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class LeadFormStatusService {
    @Autowired
    LeadRepository leadRepository;
    @Autowired
    LeadsLogRepository leadsLogRepository;

    @Transactional
    public Leads updateLeadStatus(CRMLeadFormUpdateDTO crmLeadFormUpdateDTO) {
        String leadId = crmLeadFormUpdateDTO.getLeadId();
        Optional<Leads> leadsOptional = Optional.ofNullable(leadRepository.findById(leadId));
        if (leadsOptional.isPresent()) {
            Leads leads = leadsOptional.get();
            String newStatus = crmLeadFormUpdateDTO.getStatus();
            if (newStatus != null) {
                leads.setStatus(newStatus);
            }
            LeadsLog leadsLog = new LeadsLog();
            leadsLog.setAgentId(crmLeadFormUpdateDTO.getAgentId());
            leadsLog.setLeadId(leadId);
            leadsLog.setCreatedAt(LocalDateTime.now());
            leadsLog.setStatus(crmLeadFormUpdateDTO.getStatus());
            leadsLog.setComment(crmLeadFormUpdateDTO.getComments());
            leadsLogRepository.save(leadsLog);
            return leadRepository.save(leads);
        }
        return null;
    }

    public List<LeadsLog> getLeadsLogsByLeadId(Long leadId) {
        if (leadId != null) {
            return leadsLogRepository.findByLeadId(leadId);
        } else {
            return Collections.emptyList();
        }
    }
}
