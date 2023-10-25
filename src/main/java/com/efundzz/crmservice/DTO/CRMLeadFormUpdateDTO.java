package com.efundzz.crmservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CRMLeadFormUpdateDTO {
    private Long leadId;
    private String status;
}
