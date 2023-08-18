package com.efundzz.crmservice.Mapper;

import com.efundzz.crmservice.DTO.CRMLoanDashBordResponceDTO;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public class CRMLoanDashBordResponceMapper {
    public List<CRMLoanDashBordResponceDTO> mapToDTO(List<Object[]> result) {
        return result.stream()
                .map(row -> new CRMLoanDashBordResponceDTO((String) row[0], (Long) row[1]))
                .collect(Collectors.toList());
    }
}
