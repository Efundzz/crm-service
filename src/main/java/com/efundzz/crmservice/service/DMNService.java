package com.efundzz.crmservice.service;

import com.efundzz.crmservice.DTO.CRMBreFormRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DMNService {
	private final DMNEvaluator dmnEvaluator;

    @Autowired
    private ProbabilityCalServices probabilityCalServices;
    @Autowired
    public DMNService(DMNEvaluator dmnEvaluator) {
        this.dmnEvaluator = dmnEvaluator;
    }    
    private final Logger logger = LoggerFactory.getLogger(DMNService.class);
    public List<Map<String, Object>> evaluateDecision(CRMBreFormRequestDTO inputVariables ) {
        return null;
    }  

}