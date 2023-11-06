package com.efundzz.crmservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "loans")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Loan implements Serializable {
    @Id
    @Column(name = "loan_id")
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "status")
    private String status;

    @Column(name = "process_instance_id")
    private String processInstanceId;

    @Column(name = "loan_type")
    private String loanType;

    @Column(name = "loan_sub_type")
    private String loanSubType;

    @Column(name = "brand")
    private String brand;

    @Column(name = "params")
    @Type(type = "jsonb")
    private Map<String, Object> params;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
    // Getters and setters
}
