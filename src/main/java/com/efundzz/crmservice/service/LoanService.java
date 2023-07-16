package com.efundzz.crmservice.service;


import com.efundzz.crmservice.DTO.CRMAppliacationResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.efundzz.crmservice.repository.LoanRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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