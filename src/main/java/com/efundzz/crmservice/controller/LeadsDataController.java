package com.efundzz.crmservice.controller;

import com.efundzz.crmservice.DTO.CRMLeadDataResponseDTO;
import com.efundzz.crmservice.DTO.CRMLeadFilterRequestDTO;
import com.efundzz.crmservice.DTO.CRMLeadFormRequestDTO;
import com.efundzz.crmservice.entity.Franchise;
import com.efundzz.crmservice.entity.Leads;
import com.efundzz.crmservice.service.BrandAccessService;
import com.efundzz.crmservice.service.FranchiseService;
import com.efundzz.crmservice.service.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.efundzz.crmservice.constants.AppConstants.*;

@RestController
@RequestMapping(path = "api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class LeadsDataController {
    @Autowired
    private LeadService leadService;
    @Autowired
    private FranchiseService franchiseService;
    @Autowired
    private BrandAccessService brandAccessService;

    @GetMapping("/allLeadFormData")
    @PreAuthorize("hasAuthority('read:leads')")
    public ResponseEntity<List<CRMLeadDataResponseDTO>> getLeadDataByBrand(JwtAuthenticationToken token) {
        List<String> permissions = token.getToken().getClaim(PERMISSIONS);
        String brand = determineBrandByToken(token);
        brand = EFUNDZZ_ORG.equals(brand) ? ALL_PERMISSION :brand;
        return ResponseEntity.ok(leadService.getAllLeadDataByBrand(brand));
    }

    @PostMapping("/leadFormData/filter")
    @PreAuthorize("hasAuthority('read:leads')")
    public ResponseEntity<List<Leads>> getLeadFormDataByFilter(JwtAuthenticationToken token, @RequestBody CRMLeadFilterRequestDTO filterRequest) {
        String brand = determineBrandByToken(token);
        String permitBrand = EFUNDZZ_ORG.equals(brand) ? ALL_PERMISSION : brand;
        String accessibleBrand = null;
        accessibleBrand = determineAccessibleBrand(brand, permitBrand, filterRequest.getBrand());
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
    @PreAuthorize("hasAuthority('read:leads')")
    public ResponseEntity<Leads> getLeadFormDataById(JwtAuthenticationToken token, @PathVariable Long id) {
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

    private String determineBrandByOrgId(String orgId) {
        Franchise franchise = franchiseService.getFranchisePrefixByOrgId(orgId);
        return (franchise != null) ? franchise.getFranchisePrefix() : null;
    }

    private String determineAccessibleBrand(String brand, String permit, String filterBrand) {
        return permit.equals(ALL_PERMISSION) && filterBrand.equals(ALL_PERMISSION)
                ? ALL_PERMISSION
                : permit.equals(ALL_PERMISSION)
                ? filterBrand
                : brand;
    }
    private String determineBrandByToken(JwtAuthenticationToken token) {
        String orgId = token.getToken().getClaim(ORG_ID);
        String brand = determineBrandByOrgId(orgId);
        if (brand == null) {
            throw new RuntimeException("Unauthorized access");
        }
        return brand;
    }
}
