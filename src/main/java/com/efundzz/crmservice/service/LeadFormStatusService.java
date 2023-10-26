package com.efundzz.crmservice.service;

import com.efundzz.crmservice.DTO.CRMLeadFormUpdateDTO;
import com.efundzz.crmservice.entity.Leads;
import com.efundzz.crmservice.repository.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LeadFormStatusService {
    @Autowired
    LeadRepository leadRepository;

    @Transactional
    public Leads updateLeadStatus(CRMLeadFormUpdateDTO crmLeadFormUpdateDTO) {

        Optional<Leads> optionalLead = Optional.ofNullable(leadRepository.findById(crmLeadFormUpdateDTO.getLeadId()));
        if (optionalLead.isPresent()) {
            Leads leads = optionalLead.get();
            leads.setStatus(crmLeadFormUpdateDTO.getStatus());
            return leadRepository.save(leads);
        }
        return null;
    }
}
