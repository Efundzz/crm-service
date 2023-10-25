package com.efundzz.crmservice.controller;

import com.efundzz.crmservice.DTO.CRMLeadFormUpdateDTO;
import com.efundzz.crmservice.entity.Leads;
import com.efundzz.crmservice.service.LeadFormStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.efundzz.crmservice.constants.AppConstants.PERMISSIONS;
import static com.efundzz.crmservice.utils.Brand.determineBrand;

@RestController
@RequestMapping(path = "api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class LeadFormStatuController {

    @Autowired
    private LeadFormStatusService leadFormStatusService;

    @PutMapping("/leadFormData/statusUpdate")
    public ResponseEntity<Leads> updateLeadStatus(JwtAuthenticationToken token, @RequestBody CRMLeadFormUpdateDTO updateDto) {
        List<String> permissions = token.getToken().getClaim(PERMISSIONS);
        String brand = determineBrand(permissions);
        if (brand == null) {
            throw new RuntimeException("Invalid permissions");
        }
        Leads updatedLeadStatus = leadFormStatusService.updateLeadStatus(updateDto);
        return ResponseEntity.ok(updatedLeadStatus);
    }
}
