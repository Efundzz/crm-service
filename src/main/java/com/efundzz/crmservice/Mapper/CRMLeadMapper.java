package com.efundzz.crmservice.Mapper;
import com.efundzz.crmservice.DTO.CRMLeadDataResponseDTO;
import com.efundzz.crmservice.entity.Leads;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CRMLeadMapper {
    CRMLeadDataResponseDTO mapLeadsToDTO(Leads leads);
}
