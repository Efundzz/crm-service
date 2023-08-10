package com.efundzz.crmservice.service;

import com.efundzz.crmservice.DTO.CRMLeadDataResponseDTO;
import com.efundzz.crmservice.entity.Leads;
import com.efundzz.crmservice.repository.LeadRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeadService {
    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<CRMLeadDataResponseDTO> getAllLeadDataByBrand(String brand) {
        List<Leads> entityList = leadRepository.findByBrand(brand);
        return entityList.stream()
                .map(entity -> modelMapper.map(entity, CRMLeadDataResponseDTO.class))
                .collect(Collectors.toList());
    }
}
