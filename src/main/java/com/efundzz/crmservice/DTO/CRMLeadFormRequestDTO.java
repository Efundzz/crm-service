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
public class CRMLeadFormRequestDTO {

    private Long id;
    private String city;
    private LocalDateTime createdAt;
    private String pincode;
    private String emailId;
    private String mobileNumber;
    private String name;
    private String loanType;
    private Map<String, Object> utmParams;
    private String brand;
    private Map<String, Object> additionalParams;
    private String status;
}
