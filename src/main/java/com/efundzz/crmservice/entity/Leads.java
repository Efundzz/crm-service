package com.efundzz.crmservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "leads")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Leads {
    @Id
    @GenericGenerator(name = "lead_id_gen", strategy = "com.efundzz.crmservice.service.LeadIdGenerator")
    @GeneratedValue(generator = "lead_id_gen")
    @Column(name = "lead_id")
    private String id;

    @Column(name = "city")
    private String city;

    @Column(name = "created_dt")
    private LocalDateTime createdAt;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "emailid")
    private String emailId;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "loantype")
    private String loanType;

    @Column(name = "loan_type")
    private String typeOfLoan;

    @Column(name = "utm_params")
    @Type(type = "jsonb")
    private Map<String, Object> utmParams;

    @Column(name = "brand")
    private String brand;

    @Column(name = "additional_params")
    @Type(type = "jsonb")
    private Map<String, Object> additionalParams;

    @Column(name = "status", columnDefinition = "varchar(255) default 'Pending'")
    private String status;


}
