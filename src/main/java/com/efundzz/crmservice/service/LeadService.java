package com.efundzz.crmservice.service;

import com.efundzz.crmservice.DTO.CRMLeadDataResponseDTO;
import com.efundzz.crmservice.DTO.CRMLeadFormRequestDTO;
import com.efundzz.crmservice.Mapper.CRMLeadMapper;
import com.efundzz.crmservice.Mapper.CRMStepDataMapper;
import com.efundzz.crmservice.entity.Leads;
import com.efundzz.crmservice.repository.LeadRepository;
import com.efundzz.crmservice.repository.StepDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeadService {
    @Autowired
    private LeadRepository leadRepository;
    @Autowired
    private CRMLeadMapper crmLeadMapper;
    @Autowired
    private CRMStepDataMapper crmStepDataMapper;

    @Autowired
    private StepDataRepository stepDataRepository;

    public List<CRMLeadDataResponseDTO> getAllLeadDataByBrand(String brand) {
        List<Leads> entityList;
        if (brand != null && brand.equalsIgnoreCase("ALL")) {
            entityList = leadRepository.findByBrand(null);
        } else {
            entityList = leadRepository.findByBrand(brand);
        }
        return entityList.stream()
                .map(leads -> crmLeadMapper.mapLeadsToDTO(leads))
                .collect(Collectors.toList());
    }

    public Leads getLeadFormDataById(Long id) {
        return leadRepository.findById(id);
    }

    public List<Leads> findLeadFormDataByFilter(String brand, String fromDate, String toDate,String status,String loanType) {
        List<Leads> fetchedData;
        if (brand.equalsIgnoreCase("ALL")) {
            fetchedData = leadRepository.findLeadFormDataByFilter(null,loanType, fromDate, toDate, status);
        } else {
            fetchedData = leadRepository.findLeadFormDataByFilter(brand,loanType , fromDate, toDate, status);
        }
        return fetchedData;
    }

    public Leads createLead(CRMLeadFormRequestDTO leadFormRequestDTO) {
        Long refNum = (long) (Math.random() * 100000);
        Leads lead = new Leads();
        lead.setId(refNum);
        lead.setCity(leadFormRequestDTO.getCity());
        lead.setCreatedAt(LocalDateTime.now());
        lead.setPincode(leadFormRequestDTO.getPincode());
        lead.setEmailId(leadFormRequestDTO.getEmailId());
        lead.setMobileNumber(leadFormRequestDTO.getMobileNumber());
        lead.setName(leadFormRequestDTO.getName());
        lead.setLoanType(leadFormRequestDTO.getLoanType());
        lead.setUtmParams(leadFormRequestDTO.getUtmParams());
        lead.setBrand(leadFormRequestDTO.getBrand());
        lead.setStatus(leadFormRequestDTO.getStatus());
        return leadRepository.save(lead);
    }

}

