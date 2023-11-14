package com.efundzz.crmservice.controller;

import com.efundzz.crmservice.DTO.CRMLeadFormUpdateDTO;
import com.efundzz.crmservice.entity.Franchise;
import com.efundzz.crmservice.entity.Leads;
import com.efundzz.crmservice.entity.LeadsLog;
import com.efundzz.crmservice.service.FranchiseService;
import com.efundzz.crmservice.service.LeadFormStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.efundzz.crmservice.constants.AppConstants.ORG_ID;
import static com.efundzz.crmservice.constants.AppConstants.PERMISSIONS;
import static com.efundzz.crmservice.utils.Brand.determineCreateAccess;

@RestController
@RequestMapping(path = "api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class LeadFormStatusController {

    @Autowired
    private LeadFormStatusService leadFormStatusService;
    @Autowired
    private FranchiseService franchiseService;

    @PutMapping("/leadFormData/statusUpdate")
    public ResponseEntity<Leads> updateLeadStatus(JwtAuthenticationToken token, @RequestBody CRMLeadFormUpdateDTO updateDto) {
        List<String> permissions = token.getToken().getClaim(PERMISSIONS);
        List<String> createAccess = determineCreateAccess(permissions);
        if (createAccess.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Leads updatedLeadStatus = leadFormStatusService.updateLeadStatus(updateDto);
        return ResponseEntity.ok(updatedLeadStatus);
    }
    @GetMapping("/leadFormData/statusLogs/{leadId}")
    public List<LeadsLog> getLeadsLogsByLeadId(JwtAuthenticationToken token, @PathVariable Long leadId) {
        String orgId = token.getToken().getClaim(ORG_ID);
        String brand = determineBrandByOrgId(orgId);
        if (brand == null) {
            throw new RuntimeException("Invalid permissions");
        }
        return leadFormStatusService.getLeadsLogsByLeadId(leadId);
    }

    private String determineBrandByOrgId(String orgId) {
        Franchise franchise = franchiseService.getFranchisePrefixByOrgId(orgId);
        return (franchise != null) ? franchise.getFranchisePrefix() : null;
    }

}
