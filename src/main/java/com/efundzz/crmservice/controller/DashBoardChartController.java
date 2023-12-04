package com.efundzz.crmservice.controller;

import com.efundzz.crmservice.dto.CRMLoanDashBordResponceDTO;
import com.efundzz.crmservice.service.BrandAccessService;
import com.efundzz.crmservice.service.BrandService;
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

import static com.efundzz.crmservice.constants.AppConstants.ALL_PERMISSION;
import static com.efundzz.crmservice.constants.AppConstants.EFUNDZZ_ORG;

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

    @Autowired
    private BrandService brandService;

    private LocalDateTime inputDate;


    @GetMapping("/dashBord/loanTypeCounts")
    public List<CRMLoanDashBordResponceDTO> getLeadCountByLoanType(JwtAuthenticationToken token) {
        String brand = brandService.determineBrandByToken(token);
        brand = EFUNDZZ_ORG.equals(brand) ? ALL_PERMISSION :brand;
        return dashBordChartService.getCountsByLoanType(inputDate, brand);
    }

    @GetMapping("/dashBord/statusCounts")
    public List<CRMLoanDashBordResponceDTO> getLeadsCountByStatus(JwtAuthenticationToken token) {
        String brand = brandService.determineBrandByToken(token);
        brand = EFUNDZZ_ORG.equals(brand) ? ALL_PERMISSION :brand;
        return dashBordChartService.getCountsByLoanStatus(inputDate, brand);
    }

    @GetMapping("/dashBord/brandCount")
    public List<CRMLoanDashBordResponceDTO> getLoanCountByBrand(JwtAuthenticationToken token) {
        String brand = brandService.determineBrandByToken(token);
        brand = EFUNDZZ_ORG.equals(brand) ? ALL_PERMISSION :brand;
        return dashBordChartService.getLoanCountByBrand(inputDate,brand);
    }

    @PostConstruct
    public void initializeStartDate() {
        inputDate = LocalDateTime.now().minusDays(durationInDays);
    }

}


