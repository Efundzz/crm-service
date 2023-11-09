package com.efundzz.crmservice.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // The name of the franchise, state, city, or branch
    private String type; // "FRANCHISE", "STATE", "CITY", "BRANCH"
    private String referralId; // Unique ID for referral tracking

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private OrganizationUnit parent;

    // Additional fields and relationships for leads and loans
}

