package com.efundzz.crmservice.controller;

import com.efundzz.crmservice.DTO.CRMAppliacationResponseDTO;
import com.efundzz.crmservice.DTO.CRMLeadFilterRequestDTO;
import com.efundzz.crmservice.DTO.CRMLeadDataResponseDTO;
import com.efundzz.crmservice.entity.Leads;
import com.efundzz.crmservice.service.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.efundzz.crmservice.utils.Brand.determineBrand;

@RestController
@RequestMapping(path = "api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class LeadsDataController {
    @Autowired
    private LeadService leadService;

    @GetMapping("/leads")
    public ResponseEntity<List<CRMLeadDataResponseDTO>> getLeadDataByBrand(JwtAuthenticationToken token) {
        List<String> permissions = token.getToken().getClaim("permissions");
        String brand = determineBrand(permissions);
        if (brand == null) {
            throw new RuntimeException("Invalid permissions");
        }
        System.out.println(permissions);
        return ResponseEntity.ok(leadService.getAllLeadDataByBrand(brand));
    }

    @GetMapping("/leads/{appId}")
    public ResponseEntity<List<CRMAppliacationResponseDTO>> getLeadDataByAppId(JwtAuthenticationToken token, @PathVariable String appId) {
        List<String> permissions = token.getToken().getClaim("permissions");
        String brand = determineBrand(permissions);
        if (brand == null) {
            throw new RuntimeException("Invalid permissions");
        }
        System.out.println(permissions);
        List<CRMAppliacationResponseDTO> leadData = leadService.getAllLeadDataByAppId(appId, brand);
        return ResponseEntity.ok(leadData);
    }

    @GetMapping("/leads/filter")
    public ResponseEntity<List<Leads>> getLeadFormDataByFilter(JwtAuthenticationToken token, @RequestBody CRMLeadFilterRequestDTO filterRequest) {
        List<String> permissions = token.getToken().getClaim("permissions");
        String brand = determineBrand(permissions);
        if (brand == null) {
            throw new RuntimeException("Invalid permissions");
        }
        List<Leads> filteredLeads = leadService.findLeadFormDataByFilter(
                filterRequest.getBrand(),
                filterRequest.getLoanType(),
                filterRequest.getName(),
                filterRequest.getFormDate(),
                filterRequest.getToDate());
        return ResponseEntity.ok(filteredLeads);
    }

    @GetMapping("/leadsForm/{id}")
    public ResponseEntity<Leads> getLeadFormDataById(JwtAuthenticationToken token, @PathVariable Long id) {
        List<String> permissions = token.getToken().getClaim("permissions");
        String brand = determineBrand(permissions);
        if (brand == null) {
            throw new RuntimeException("Invalid permissions");
        }
        Leads lead = leadService.getLeadFormDataById(id);
        if (lead != null) {
            return ResponseEntity.ok(lead);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}