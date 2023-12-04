package com.efundzz.crmservice.controller;

import com.efundzz.crmservice.dto.CRMLeadDataResponseDTO;
import com.efundzz.crmservice.dto.CRMLeadFilterRequestDTO;
import com.efundzz.crmservice.dto.CRMLeadFormRequestDTO;
import com.efundzz.crmservice.entity.Leads;
import com.efundzz.crmservice.service.BrandService;
import com.efundzz.crmservice.service.FranchiseService;
import com.efundzz.crmservice.service.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.efundzz.crmservice.constants.AppConstants.ALL_PERMISSION;
import static com.efundzz.crmservice.constants.AppConstants.EFUNDZZ_ORG;

@RestController
@RequestMapping(path = "api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class LeadsDataController {
    @Autowired
    private LeadService leadService;
    @Autowired
    private FranchiseService franchiseService;
    @Autowired
    private BrandService brandService;

    @GetMapping("/allLeadFormData")
    @PreAuthorize("hasAuthority('read:leads')")
    public ResponseEntity<List<CRMLeadDataResponseDTO>> getLeadDataByBrand(JwtAuthenticationToken token) {
        String brand = brandService.determineBrandByToken(token);
        brand = EFUNDZZ_ORG.equals(brand) ? ALL_PERMISSION :brand;
        return ResponseEntity.ok(leadService.getAllLeadDataByBrand(brand));
    }

    @PostMapping("/leadFormData/filter")
    @PreAuthorize("hasAuthority('read:leads')")
    public ResponseEntity<List<Leads>> getLeadFormDataByFilter(JwtAuthenticationToken token, @RequestBody CRMLeadFilterRequestDTO filterRequest) {
        String brand = brandService.determineBrandByToken(token);
        String permitBrand = EFUNDZZ_ORG.equals(brand) ? ALL_PERMISSION : brand;
        String accessibleBrand = null;
        accessibleBrand = brandService.determineAccessibleBrand(brand, permitBrand, filterRequest.getBrand());
        List<Leads> filteredLeads = null;
        if (accessibleBrand != null) {
            filteredLeads = leadService.findLeadFormDataByFilter(
                    accessibleBrand,
                    filterRequest.getLoanType(),
                    filterRequest.getFromDate(),
                    filterRequest.getToDate(),
                    filterRequest.getStatus());
        }
        return ResponseEntity.ok(filteredLeads);
    }
    @GetMapping("/getLeadsFormData/{id}")
    public ResponseEntity<Leads> getLeadFormDataById(JwtAuthenticationToken token, @PathVariable String id) {
        Leads lead = leadService.getLeadFormDataById(id);
        if (lead != null) {
            return ResponseEntity.ok(lead);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/leadFormData/createLead")
    @PreAuthorize("hasAuthority('write:leads')")
    public Leads createLead(JwtAuthenticationToken token,@RequestBody CRMLeadFormRequestDTO leadFormRequestDTO) {
        return leadService.createLead(leadFormRequestDTO);
    }

}
