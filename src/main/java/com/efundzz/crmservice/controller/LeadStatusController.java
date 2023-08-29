package com.efundzz.crmservice.controller;


import com.efundzz.crmservice.DTO.CRMLeadStatusDTO;
import com.efundzz.crmservice.entity.LeadStatus;
import com.efundzz.crmservice.service.LeadStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class LeadStatusController {

    @Autowired
    private LeadStatusService leadStatusService;

    @PutMapping("/updateLeadStatus")
    public ResponseEntity<LeadStatus> updateLeadStatus(@RequestBody CRMLeadStatusDTO crmLeadStatusDTO) {
        LeadStatus updatedLeadStatus = leadStatusService.updateLeadStatus(crmLeadStatusDTO);
        return ResponseEntity.ok(updatedLeadStatus);
    }
}
