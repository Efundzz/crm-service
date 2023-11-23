package com.efundzz.crmservice.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DecisionResultDTO {
	private String entity;
    private double proposedEmi;
    private double roi;
    private double tenure;

//	public Map<String, Object> toMap() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("entity", this.entity);
//        map.put("proposed emi", this.proposedEmi);
//        map.put("roi", this.roi);
//        map.put("tenure", this.tenure);
//        return map;
//    }
}