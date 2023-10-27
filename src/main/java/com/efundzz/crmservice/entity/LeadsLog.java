package com.efundzz.crmservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "leads_log")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeadsLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long id;

    @Column(name = "lead_id")
    private Long leadId;

    @Column(name = "agent_id")
    private String agentId;

    @Column(name = "status")
    private String status;

    @Column(name = "created_dt")
    private LocalDateTime createdAt;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;
}
