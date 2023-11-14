package com.efundzz.crmservice.controller;

import com.efundzz.crmservice.DTO.CRMLoanDashBordResponceDTO;
import com.efundzz.crmservice.entity.Franchise;
import com.efundzz.crmservice.service.BrandAccessService;
import com.efundzz.crmservice.service.DashBordChartService;
import com.efundzz.crmservice.service.FranchiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

import static com.efundzz.crmservice.constants.AppConstants.*;
import static com.efundzz.crmservice.utils.Brand.determineReadAccess;

@RestController
@RequestMapping(path = "api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class DashBoardChartController {
    @Value("${custom.duration.days}")
    private int durationInDays;
    @Autowired
    private DashBordChartService dashBordChartService;
    @Autowired
    private FranchiseService franchiseService;
    
    @Autowired
    private BrandAccessService brandAccessService;
    private LocalDateTime inputDate;

    @GetMapping("/dashBord/loanTypeCounts")
    public List<CRMLoanDashBordResponceDTO> getLeadCountByLoanType(JwtAuthenticationToken token) {
        List<String> permissions = token.getToken().getClaim(PERMISSIONS);
        String brand = determineBrandByToken(token);
        List<String> readBrands = determineReadAccess(permissions);
        brand = readBrands.contains(ALL_PERMISSION) ? ALL_PERMISSION: brand;
        return dashBordChartService.getCountsByLoanType(inputDate, brand);
    }

    @GetMapping("/dashBord/statusCounts")
    public List<CRMLoanDashBordResponceDTO> getLeadsCountByStatus(JwtAuthenticationToken token) {
        List<String> permissions = token.getToken().getClaim(PERMISSIONS);
        String brand = determineBrandByToken(token);
        List<String> readBrands = determineReadAccess(permissions);
        brand = readBrands.contains(ALL_PERMISSION) ? ALL_PERMISSION: brand;
        return dashBordChartService.getCountsByLoanStatus(inputDate, brand);
    }

    @GetMapping("/dashBord/brandCount")
    public List<CRMLoanDashBordResponceDTO> getLoanCountByBrand(JwtAuthenticationToken token) {
        List<String> permissions = token.getToken().getClaim(PERMISSIONS);
        String brand = determineBrandByToken(token);
        List<String> readBrands = determineReadAccess(permissions);
        brand = readBrands.contains(ALL_PERMISSION) ? ALL_PERMISSION: brand;
        return dashBordChartService.getLoanCountByBrand(inputDate,brand);
    }

    private String determineBrandByToken(JwtAuthenticationToken token) {
        String orgId = token.getToken().getClaim(ORG_ID);
        String brand = determineBrandByOrgId(orgId);
        if (brand == null) {
            throw new RuntimeException("Unauthorized access");
        }
        return brand;
    }

    private String determineBrandByOrgId(String orgId) {
        Franchise franchise = franchiseService.getFranchisePrefixByOrgId(orgId);
        return (franchise != null) ? franchise.getFranchisePrefix() : null;
    }

    @PostConstruct
    public void initializeStartDate() {
        inputDate = LocalDateTime.now().minusDays(durationInDays);
    }

}


