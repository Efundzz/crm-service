package com.efundzz.crmservice.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CRMLoanDashBordResponceDTO {
    private String dataValue;
    private Long count;
}
