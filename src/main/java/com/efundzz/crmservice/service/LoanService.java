package com.efundzz.crmservice.service;


import com.efundzz.crmservice.DTO.CRMAppliacationResponseDTO;
import com.efundzz.crmservice.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    public List<CRMAppliacationResponseDTO> getAllLoanDataWithMergedStepData(String brand) {
        List<CRMAppliacationResponseDTO> fetchedData;
        if (brand.equalsIgnoreCase("ALL")) {
            fetchedData = loanRepository.findAllWithStepData(null);
        } else {
            fetchedData = loanRepository.findAllWithStepData(brand);
        }

        Map<String, CRMAppliacationResponseDTO> resultMap = new LinkedHashMap<>();

        for (CRMAppliacationResponseDTO item : fetchedData) {
            if (item != null && item.getData() != null) {
                if (!resultMap.containsKey(item.getId())) {
                    resultMap.put(item.getId(), item);
                } else {
                    CRMAppliacationResponseDTO existingItem = resultMap.get(item.getId());
                    if (existingItem != null && existingItem.getData() != null) {
                        existingItem.getData().putAll(item.getData());
                    }
                }
            }
        }
        return new ArrayList<>(resultMap.values());
    }


    //findAllStepDataByCriteria

    public List<CRMAppliacationResponseDTO> findApplicationsByFilter(String brand, String loanType, String fromDate, String toDate, String name) {
        List<CRMAppliacationResponseDTO> fetchedData;
        if (brand.equalsIgnoreCase("ALL")) {
            fetchedData = loanRepository.findAllStepDataByCriteria(null, loanType, fromDate, toDate, name);
        } else {
            fetchedData = loanRepository.findAllStepDataByCriteria(brand, loanType, fromDate, toDate, name);
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

}