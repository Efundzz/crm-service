package com.efundzz.crmservice.service;

import com.efundzz.crmservice.DTO.CRMLeadDataResponseDTO;
import com.efundzz.crmservice.DTO.CRMLeadDetailsResponseDTO;
import com.efundzz.crmservice.Mapper.CRMLeadMapper;
import com.efundzz.crmservice.Mapper.CRMStepDataMapper;
import com.efundzz.crmservice.entity.Leads;
import com.efundzz.crmservice.entity.StepData;
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
        List<Leads> entityList = leadRepository.findByBrand(brand);
        return entityList.stream()
                .map(leads -> crmLeadMapper.mapLeadsToDTO(leads))
                .collect(Collectors.toList());
    }
    public List<CRMLeadDetailsResponseDTO> getAllLeadDataByAppId(String appId) {
        List<StepData> entityList = stepDataRepository.findByApplicationId(appId);
        return entityList.stream()
                .map( stepData-> crmStepDataMapper.mapStepDataToDTO(stepData))
                .collect(Collectors.toList());
    }
}
