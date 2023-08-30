package com.efundzz.crmservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CRMLeadStatusDTO {

    private String loanId;

    private String agentId;

    private String loanType;

    private String brand;

    private String status;

    private LocalDateTime updatedAt;

    private String comments;
}
