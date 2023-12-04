package com.efundzz.crmservice.mapper;
import com.efundzz.crmservice.dto.CRMLeadDataResponseDTO;
import com.efundzz.crmservice.entity.Leads;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CRMLeadMapper {
    CRMLeadDataResponseDTO mapLeadsToDTO(Leads leads);
}
