package com.efundzz.crmservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrmUserRequestDTO {
    private String userName;
    private String fullName;
    private String email;
    private Long franchiseId;
    private String password;
}
