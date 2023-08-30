package com.efundzz.crmservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lead_status")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeadStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long id;

    @Column(name = "loan_id")
    private String loanId;

    @Column(name = "agent_id")
    private String agentId;

    @Column(name = "loan_type")
    private String loanType;

    @Column(name = "brand")
    private String brand;

    @Column(name = "status")
    private String status;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "comments")
    private String comments;
}
