package com.efundzz.crmservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CRMAppliacationResponseDTO {

    private String id; // from Loan's loan_id
    private String userId;
    private String status;
    private String loanType;
    private String loanSubType;
    private String brand;
    private LocalDateTime createdAt;
    private Map<String, Object> data;

    public CRMAppliacationResponseDTO(String id, String userId, String status, String loanType,String loanSubType, String brand ,LocalDateTime createdAt,Object data) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.loanType = loanType;
        this.loanSubType = loanSubType;
        this.brand = brand;
        this.data = (Map<String, Object>) data; // Cast Object to Map<String, Object>
        this.createdAt = createdAt;
    }
}