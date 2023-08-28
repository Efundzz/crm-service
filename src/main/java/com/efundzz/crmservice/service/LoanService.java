package com.efundzz.crmservice.service;


import com.efundzz.crmservice.DTO.CRMAppliacationResponseDTO;
import com.efundzz.crmservice.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        Map<String, CRMAppliacationResponseDTO> resultMap = fetchedData.stream()
                .filter(item -> item != null && item.getData() != null)
                .collect(Collectors.toMap(
                        CRMAppliacationResponseDTO::getId,
                        Function.identity(),
                        (existingItem, newItem) -> {
                            existingItem.getData().putAll(newItem.getData());
                            return existingItem;
                        }
                ));
        return new ArrayList<>(resultMap.values());
    }


    //findAllStepDataByCriteria

    public List<CRMAppliacationResponseDTO> findApplicationsByFilter(String brand, String loanType, String fromDate, String toDate, String loanStatus) {
        List<CRMAppliacationResponseDTO> fetchedData;
        if (brand.equalsIgnoreCase("ALL")) {
            fetchedData = loanRepository.findAllStepDataByCriteria(null, loanType, fromDate, toDate, loanStatus);
        } else {
            fetchedData = loanRepository.findAllStepDataByCriteria(brand, loanType, fromDate, toDate, loanStatus);
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