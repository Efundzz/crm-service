package com.efundzz.crmservice.service;

import com.efundzz.crmservice.dto.CRMLeadDataResponseDTO;
import com.efundzz.crmservice.dto.CRMLeadFormRequestDTO;
import com.efundzz.crmservice.mapper.CRMLeadMapper;
import com.efundzz.crmservice.mapper.CRMStepDataMapper;
import com.efundzz.crmservice.entity.Leads;
import com.efundzz.crmservice.repository.LeadRepository;
import com.efundzz.crmservice.repository.StepDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.efundzz.crmservice.constants.AppConstants.PENDING;

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

    public Leads getLeadFormDataById(String id) {
        return leadRepository.findById(id);
    }

    public List<Leads> findLeadFormDataByFilter(String brand, String loanType, String fromDate, String toDate, String status) {
        //        if (loanType != null) {
//            List<Leads> filteredData = new ArrayList<>();
//            for (Leads lead : fetchedData) {
//                Map<String, Object> additionalParams = lead.getAdditionalParams();
//                if (additionalParams != null) {
//                    Object typeOfLoanValue = additionalParams.get("typeOfLoan");
//                    if (typeOfLoanValue != null) {
//                        String typeOfLoan = typeOfLoanValue.toString();
//                        if (typeOfLoan.startsWith(loanType) && typeOfLoan.substring(0, loanType.length()).equalsIgnoreCase(loanType)) {
//                            filteredData.add(lead);
//                        }
//                    }
//                }
//            }
//            return filteredData;
//        }
        return (brand.equalsIgnoreCase("ALL"))
                ? leadRepository.findLeadFormDataByFilter(null,loanType, fromDate, toDate, status)
                : leadRepository.findLeadFormDataByFilter(brand, loanType,fromDate, toDate, status);
    }

    public Leads createLead(CRMLeadFormRequestDTO leadFormRequestDTO) {
        Leads lead = new Leads();
        lead.setCity(leadFormRequestDTO.getCity());
        lead.setCreatedAt(LocalDateTime.now());
        lead.setPincode(leadFormRequestDTO.getPincode());
        lead.setEmailId(leadFormRequestDTO.getEmailId());
        lead.setMobileNumber(leadFormRequestDTO.getMobileNumber());
        lead.setName(leadFormRequestDTO.getName());
        lead.setLoanType(leadFormRequestDTO.getLoanType());
        lead.setUtmParams(leadFormRequestDTO.getUtmParams());
        lead.setAdditionalParams(leadFormRequestDTO.getAdditionalParams());
        lead.setBrand(leadFormRequestDTO.getBrand());
        lead.setStatus(PENDING);
        return leadRepository.save(lead);
    }

}

