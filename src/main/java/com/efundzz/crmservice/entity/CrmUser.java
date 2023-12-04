package com.efundzz.crmservice.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrmUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String fullName;
    private String email;
    @ManyToOne
    @JoinColumn(name = "franchiseId")
    private Franchise franchise;
}
