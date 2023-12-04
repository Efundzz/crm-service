package com.efundzz.crmservice.controller;

import com.efundzz.crmservice.dto.CRMLeadFormUpdateDTO;
import com.efundzz.crmservice.entity.Leads;
import com.efundzz.crmservice.entity.LeadsLog;
import com.efundzz.crmservice.service.BrandService;
import com.efundzz.crmservice.service.FranchiseService;
import com.efundzz.crmservice.service.LeadFormStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class LeadFormStatusController {

    @Autowired
    private LeadFormStatusService leadFormStatusService;
    @Autowired
    private FranchiseService franchiseService;

    @Autowired
    private BrandService brandService;

    @PutMapping("/leadFormData/statusUpdate")
    @PreAuthorize("hasAuthority('write:leads')")
    public ResponseEntity<Leads> updateLeadStatus(JwtAuthenticationToken token, @RequestBody CRMLeadFormUpdateDTO updateDto) {
        Leads updatedLeadStatus = leadFormStatusService.updateLeadStatus(updateDto);
        return ResponseEntity.ok(updatedLeadStatus);
    }
    @GetMapping("/leadFormData/statusLogs/{leadId}")
    public List<LeadsLog> getLeadsLogsByLeadId(JwtAuthenticationToken token, @PathVariable Long leadId) {
        return leadFormStatusService.getLeadsLogsByLeadId(leadId);
    }
}
