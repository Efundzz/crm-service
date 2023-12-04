package com.efundzz.crmservice.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrmLogin {
    @Id
    private Long userId;
    private String userName;
    private String passwordHash;
}
