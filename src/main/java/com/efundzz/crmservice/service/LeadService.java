package com.efundzz.crmservice.service;

import com.efundzz.crmservice.DTO.CRMLeadDataResponseDTO;
import com.efundzz.crmservice.Mapper.CRMLeadMapper;
import com.efundzz.crmservice.Mapper.CRMStepDataMapper;
import com.efundzz.crmservice.entity.Leads;
import com.efundzz.crmservice.repository.LeadRepository;
import com.efundzz.crmservice.repository.StepDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Leads> findLeadFormDataByFilter(String brand, String loanType, String name, String fromDate, String toDate,String todayDate) {
        List<Leads> fetchedData;
        if (brand.equalsIgnoreCase("ALL")) {
            fetchedData = leadRepository.findLeadFormDataByFilter(null, loanType, name, fromDate, toDate,todayDate);
        } else {
            fetchedData = leadRepository.findLeadFormDataByFilter(brand, loanType, name, fromDate, toDate,todayDate);
        }
        return fetchedData;
    }
}

