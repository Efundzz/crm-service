package com.efundzz.crmservice.service;

import com.efundzz.crmservice.DTO.CRMAppliacationResponseDTO;
import com.efundzz.crmservice.DTO.CRMLeadDataResponseDTO;
import com.efundzz.crmservice.Mapper.CRMLeadMapper;
import com.efundzz.crmservice.Mapper.CRMStepDataMapper;
import com.efundzz.crmservice.entity.Leads;
import com.efundzz.crmservice.repository.LeadRepository;
import com.efundzz.crmservice.repository.StepDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    public Leads getLeadFormDataById(Long id) {
        return leadRepository.findById(id);
    }

    public List<CRMAppliacationResponseDTO> getAllLeadDataByAppId(String appId, String brand) {

        List<CRMAppliacationResponseDTO> fetchedData;
        if (brand.equalsIgnoreCase("ALL")) {
            fetchedData = stepDataRepository.findLeadDataByApplicationId(appId, null);
        } else {
            fetchedData = stepDataRepository.findLeadDataByApplicationId(appId, brand);
        }
        Map<String, CRMAppliacationResponseDTO> resultMap = new LinkedHashMap<>();

        for (CRMAppliacationResponseDTO item : fetchedData) {
            if (!resultMap.containsKey(item.getId())) {
                resultMap.put(item.getId(), item);
            } else {
                CRMAppliacationResponseDTO existingItem = resultMap.get(item.getId());
                existingItem.getData().putAll(item.getData());
            }
        }
        return new ArrayList<>(resultMap.values());
    }

    public List<Leads> findLeadFormDataByFilter(String brand, String loanType, String name, String fromDate, String toDate) {
        List<Leads> fetchedData;
        if (brand.equalsIgnoreCase("ALL")) {
            fetchedData = leadRepository.findLeadFormDataByFilter(null, loanType, name, fromDate, toDate);
        } else {
            fetchedData = leadRepository.findLeadFormDataByFilter(brand, loanType, name, fromDate, toDate);
        }
        return fetchedData;
    }
}

