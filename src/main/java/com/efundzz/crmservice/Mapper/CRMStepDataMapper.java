package com.efundzz.crmservice.Mapper;

import com.efundzz.crmservice.DTO.CRMLeadDetailsResponseDTO;
import com.efundzz.crmservice.entity.StepData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CRMStepDataMapper {
    CRMLeadDetailsResponseDTO mapStepDataToDTO(StepData stepData);
}
