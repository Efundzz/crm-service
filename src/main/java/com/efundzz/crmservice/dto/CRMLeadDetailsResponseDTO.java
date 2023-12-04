package com.efundzz.crmservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CRMLeadDetailsResponseDTO {
    private Long id;
    private String applicationId;
    private String stepName;
    private Map<String, Object> data;
    private Date createdDT;
}
