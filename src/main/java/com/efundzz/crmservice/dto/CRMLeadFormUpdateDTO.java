package com.efundzz.crmservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CRMLeadFormUpdateDTO {
    private String leadId;
    private String status;
    private String agentId;
    private LocalDateTime updatedAt;
    private String comments;
}
