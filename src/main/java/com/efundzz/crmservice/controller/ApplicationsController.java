package com.efundzz.crmservice.controller;

import com.efundzz.crmservice.DTO.CRMAppliacationResponseDTO;
import com.efundzz.crmservice.DTO.CRMLeadFilterRequestDTO;
import com.efundzz.crmservice.entity.Franchise;
import com.efundzz.crmservice.entity.Loan;
import com.efundzz.crmservice.service.BrandAccessService;
import com.efundzz.crmservice.service.FranchiseService;
import com.efundzz.crmservice.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
public class ApplicationsController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private FranchiseService franchiseService;

    @Autowired
    private BrandAccessService brandAccessService;

    @GetMapping("/applications")
    @PreAuthorize("hasAuthority('read:applications')")
    public ResponseEntity<List<CRMAppliacationResponseDTO>> getApplications(JwtAuthenticationToken token) {
        String brand = determineBrandByToken(token);
        brand = EFUNDZZ_ORG.equals(brand) ? ALL_PERMISSION :brand;
        return ResponseEntity.ok(loanService.getAllLoanDataWithMergedStepData(brand));
    }

    @PostMapping("/applications/filter")
    @PreAuthorize("hasAuthority('read:applications')")
    public ResponseEntity<List<CRMAppliacationResponseDTO>> getApplicationsDataByFilter(JwtAuthenticationToken token, @RequestBody CRMLeadFilterRequestDTO filterRequest) {
        try {
            String brand = determineBrandByToken(token);
            String permitBrand = EFUNDZZ_ORG.equals(brand) ? ALL_PERMISSION : brand;
            String accessibleBrand = null;
            accessibleBrand = determineAccessibleBrand(brand, permitBrand, filterRequest.getBrand());
            List<CRMAppliacationResponseDTO> filteredApplications = null;
            if (accessibleBrand != null) {
                filteredApplications = loanService.findApplicationsByFilter(
                        accessibleBrand,
                        filterRequest.getLoanType(),
                        filterRequest.getFromDate(),
                        filterRequest.getToDate(),
                        filterRequest.getLoanStatus());
            }
            return ResponseEntity.ok(filteredApplications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getApplicationsData/{appId}")
    @PreAuthorize("hasAuthority('read:applications')")
    public ResponseEntity<List<CRMAppliacationResponseDTO>> getLeadDataByAppId(JwtAuthenticationToken token, @PathVariable String appId) {
        String brand = determineBrandByToken(token);
        List<CRMAppliacationResponseDTO> leadData = loanService.getAllLeadDataByAppId(appId, brand);
        return ResponseEntity.ok(leadData);
    }

    @GetMapping("/getApplicationsStatus/{loanId}")
    @PreAuthorize("hasAuthority('read:applications')")
    public ResponseEntity<Loan> getStatusByLoanID(JwtAuthenticationToken token, @PathVariable String loanId) {
        String brand = determineBrandByToken(token);
        return ResponseEntity.ok(loanService.getLoanDetailsByLoanID(loanId));
    }

    private String determineBrandByToken(JwtAuthenticationToken token) {
        String orgId = token.getToken().getClaim(ORG_ID);
        String brand = determineBrandByOrgId(orgId);
        if (brand == null) {
            throw new RuntimeException("Unauthorized access");
        }
        return brand;
    }

    private String determineAccessibleBrand(String brand, String permit, String filterBrand) {
        return permit.equals(ALL_PERMISSION) && filterBrand.equals(ALL_PERMISSION)
                ? ALL_PERMISSION
                : permit.equals(ALL_PERMISSION)
                ? filterBrand
                : brand;
    }

    private String determineBrandByOrgId(String orgId) {
        Franchise franchise = franchiseService.getFranchisePrefixByOrgId("org_7ve249G78zoYMLKs");
        return (franchise != null) ? franchise.getFranchisePrefix() : null;
    }

}


