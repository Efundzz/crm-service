package com.efundzz.crmservice.controller;

import com.efundzz.crmservice.DTO.CRMBreFormRequestDTO;
import com.efundzz.crmservice.entity.DMNEvaluationData;
import com.efundzz.crmservice.exceptions.ValidationException;
import com.efundzz.crmservice.repository.DMNEvaluationDataRepository;
import com.efundzz.crmservice.service.DMNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping(path = "api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class DMNController {
    @Autowired
    private DMNService dmnService;
    @Autowired
    private DMNEvaluationDataRepository dmnDataRepository;
    @PostMapping("/bre/evaluation")
    @PreAuthorize("hasAuthority('write:bre')")
    public ResponseEntity<?> evaluateDecision(JwtAuthenticationToken token ,@RequestBody CRMBreFormRequestDTO requestDTO) {
        try {
            System.out.println("Input Variables: " + requestDTO);
            // Assuming the third parameter DMNEvaluationDTO is not required
            List<Map<String,Object>> decisionResult = dmnService.evaluateDecision( requestDTO);
            System.out.println("Decision Result: " + decisionResult);
            if (decisionResult != null && decisionResult.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else if (decisionResult != null) {
                return ResponseEntity.ok(decisionResult);
            }
        } catch (ValidationException ex) {
            Map<String, String> fieldErrors = ex.getFieldErrors();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(fieldErrors);
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return null;
    }


    @GetMapping("/bre/getAllEvaluationData")
    @PreAuthorize("hasAuthority('write:bre')")
    public ResponseEntity<List<DMNEvaluationData>> getEvaluationData() {
        List<DMNEvaluationData> evaluationDataList = dmnDataRepository.findAll();
        return ResponseEntity.ok(evaluationDataList);
    }

}
