package com.efundzz.crmservice.controller;

import com.efundzz.crmservice.DTO.CRMLeadDataResponseDTO;
import com.efundzz.crmservice.DTO.CRMLeadFilterRequestDTO;
import com.efundzz.crmservice.DTO.CRMLeadFormRequestDTO;
import com.efundzz.crmservice.entity.Leads;
import com.efundzz.crmservice.service.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.efundzz.crmservice.constants.AppConstants.ALL_PERMISSION;
import static com.efundzz.crmservice.constants.AppConstants.PERMISSIONS;
import static com.efundzz.crmservice.utils.Brand.determineBrand;
import static com.efundzz.crmservice.utils.Brand.determineWriteBrand;

@RestController
@RequestMapping(path = "api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class LeadsDataController {
    @Autowired
    private LeadService leadService;

    @GetMapping("/allLeadFormData")
    public ResponseEntity<List<CRMLeadDataResponseDTO>> getLeadDataByBrand(JwtAuthenticationToken token) {
        List<String> permissions = token.getToken().getClaim(PERMISSIONS);
        String brand = determineBrand(permissions);
        if (brand == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        System.out.println(permissions);
        return ResponseEntity.ok(leadService.getAllLeadDataByBrand(brand));
    }

    @PostMapping("/leadFormData/filter")
    public ResponseEntity<List<Leads>> getLeadFormDataByFilter(JwtAuthenticationToken token, @RequestBody CRMLeadFilterRequestDTO filterRequest) {
        List<String> permissions = token.getToken().getClaim(PERMISSIONS);
        String brand = determineBrand(permissions);
        if (brand == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String accessibleBrand = determineAccessibleBrand(brand, filterRequest.getBrand());
        List<Leads> filteredLeads = leadService.findLeadFormDataByFilter(
                accessibleBrand,
                filterRequest.getLoanType(),
                filterRequest.getName(),
                filterRequest.getFromDate(),
                filterRequest.getToDate(),
                filterRequest.getStatus());
        return ResponseEntity.ok(filteredLeads);
    }

    @GetMapping("/getLeadsFormData/{id}")
    public ResponseEntity<Leads> getLeadFormDataById(JwtAuthenticationToken token, @PathVariable Long id) {
        List<String> permissions = token.getToken().getClaim(PERMISSIONS);
        String brand = determineBrand(permissions);
        if (brand == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Leads lead = leadService.getLeadFormDataById(id);
        if (lead != null) {
            return ResponseEntity.ok(lead);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/leadFormData/createLead")
    public Leads createLead(JwtAuthenticationToken token,@RequestBody CRMLeadFormRequestDTO leadFormRequestDTO) {
        List<String> permissions = token.getToken().getClaim(PERMISSIONS);
        String writeBrand = determineWriteBrand(permissions);
        if (writeBrand == null || writeBrand.isEmpty()) {
            throw new RuntimeException("Invalid permissions");
        }
        return leadService.createLead(leadFormRequestDTO);
    }

    private String determineAccessibleBrand(String brand, String filterBrand) {
        return (brand.equals(ALL_PERMISSION) && Objects.equals(filterBrand, ALL_PERMISSION))
                ? ALL_PERMISSION
                : (brand.equals(ALL_PERMISSION) && !Objects.equals(filterBrand, ALL_PERMISSION))
                ? filterBrand
                : brand;
    }
}
