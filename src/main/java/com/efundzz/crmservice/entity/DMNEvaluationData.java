package com.efundzz.crmservice.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "dmn_evaluation_data")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class DMNEvaluationData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String creditScore;
	private long takeHomeSalaryMonthly;
	private int age;
	private long experience;
	private String residentType;
	private long loanAmount;
	private int foir;
	private String companyCategory;
	private String salaryCreditType;
	private String ownHouse;
	private int jobStability;
	private List<Map<String, Object>> response;
	private LocalDateTime createdAt;
	private String brand;
	private String agentId;

}
