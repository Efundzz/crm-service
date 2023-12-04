package com.efundzz.crmservice.controller;


import com.efundzz.crmservice.dto.CRMLeadStatusDTO;
import com.efundzz.crmservice.entity.LeadStatus;
import com.efundzz.crmservice.service.LeadStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class LeadStatusController {

    @Autowired
    private LeadStatusService leadStatusService;

    @PutMapping("/updateLeadStatus")
    @PreAuthorize("hasAuthority('write:applications')")
    public ResponseEntity<LeadStatus> updateLeadStatus(@RequestBody CRMLeadStatusDTO crmLeadStatusDTO) {
        LeadStatus updatedLeadStatus = leadStatusService.updateLeadStatus(crmLeadStatusDTO);
        return ResponseEntity.ok(updatedLeadStatus);
    }
}
