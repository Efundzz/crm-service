package com.efundzz.crmservice.mapper;

import com.efundzz.crmservice.dto.CRMLeadDetailsResponseDTO;
import com.efundzz.crmservice.entity.StepData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CRMStepDataMapper {
    CRMLeadDetailsResponseDTO mapStepDataToDTO(StepData stepData);
}
