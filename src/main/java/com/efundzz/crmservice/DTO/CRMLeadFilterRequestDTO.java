package com.efundzz.crmservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CRMLeadFilterRequestDTO {
    private String brand;
    private String loanType;
    private String loanStatus;
    private String fromDate;
    private String toDate;
    private String name;
    private String source;
    private String status;
}
