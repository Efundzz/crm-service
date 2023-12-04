package com.efundzz.crmservice.service;


import com.efundzz.crmservice.dto.CRMAppliacationResponseDTO;
import com.efundzz.crmservice.entity.LeadStatus;
import com.efundzz.crmservice.entity.Loan;
import com.efundzz.crmservice.repository.LeadStatusRepository;
import com.efundzz.crmservice.repository.LoanRepository;
import com.efundzz.crmservice.repository.StepDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LeadStatusRepository leadStatusRepository;

    @Autowired
    private StepDataRepository stepDataRepository;

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

    public List<CRMAppliacationResponseDTO> findApplicationsByFilter(String brand, String loanType, String fromDate, String toDate, String loanStatus) {
        List<CRMAppliacationResponseDTO> fetchedData;
        if (brand.equalsIgnoreCase("ALL")) {
            fetchedData = loanRepository.findAllStepDataByCriteria(null, loanType, fromDate, toDate, loanStatus);
        } else {
            fetchedData = loanRepository.findAllStepDataByCriteria(brand, loanType, fromDate, toDate,loanStatus);
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

    public List<CRMAppliacationResponseDTO> getAllLeadDataByAppId(String appId, String brand) {

        List<CRMAppliacationResponseDTO> fetchedData;
        if (brand.equalsIgnoreCase("ALL")) {
            fetchedData = stepDataRepository.findLeadDataByApplicationId(appId, null);
        } else {
            fetchedData = stepDataRepository.findLeadDataByApplicationId(appId, brand);
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

    public Loan getLoanDetailsByLoanID(String loanID) {
        Optional<LeadStatus> optionalLeadStatus = Optional.ofNullable(leadStatusRepository.findByLoanId(loanID));

        if (optionalLeadStatus.isPresent()) {
            Loan loanWithStatus = new Loan();
            loanWithStatus.setBrand(optionalLeadStatus.get().getBrand());
            loanWithStatus.setBrand(optionalLeadStatus.get().getComments());
            loanWithStatus.setBrand(optionalLeadStatus.get().getLoanId());
            loanWithStatus.setStatus(optionalLeadStatus.get().getStatus());
            loanWithStatus.setCreatedAt(optionalLeadStatus.get().getUpdatedAt());
            return loanWithStatus;
        } else {
            Optional<Loan> optionalLoan = Optional.ofNullable(loanRepository.findById(loanID));
            if (optionalLoan.isPresent()) {
                Loan loan = optionalLoan.get();
                LeadStatus leadStatus = new LeadStatus();
                leadStatus.setLoanId(loan.getId());
                leadStatus.setStatus(loan.getStatus());
                leadStatus.setBrand(loan.getBrand());
                leadStatus.setLoanType(loan.getLoanType());
                leadStatus.setUpdatedAt(LocalDateTime.now());
                leadStatusRepository.save(leadStatus);
                return loan;
            } else {
                throw new NotFoundException("Loan with ID " + loanID + " not found");
            }
        }
    }
}