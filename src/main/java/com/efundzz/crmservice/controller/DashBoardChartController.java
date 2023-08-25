package com.efundzz.crmservice.controller;

import com.efundzz.crmservice.DTO.CRMLoanDashBordResponceDTO;
import com.efundzz.crmservice.service.DashBordChartService;
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

import static com.efundzz.crmservice.utils.Brand.determineBrand;

@RestController
@RequestMapping(path = "api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class DashBoardChartController {
       @Value("${custom.duration.days}")
        private int durationInDays;
        @Autowired
        private DashBordChartService dashBordChartService;
        private LocalDateTime inputDate;
        @GetMapping("dashBord/loanTypeCounts")
        public List<CRMLoanDashBordResponceDTO> getLeadCountByLoanType(JwtAuthenticationToken token) {
            List<String> permissions = token.getToken().getClaim("permissions");
            String brand = determineBrand(permissions);
            if (brand == null) {
                throw new RuntimeException("Invalid permissions"); // Adjust error handling as needed.
            }
            System.out.println(permissions);
            return dashBordChartService.getCountsByLoanType(inputDate,brand);
        }
        @GetMapping("dashBord/statusCounts")
        public List<CRMLoanDashBordResponceDTO> getLeadsCountByStatus(JwtAuthenticationToken token) {
            List<String> permissions = token.getToken().getClaim("permissions");
            String brand = determineBrand(permissions);
            if (brand == null) {
                throw new RuntimeException("Invalid permissions"); // Adjust error handling as needed.
            }
            System.out.println(permissions);
            return dashBordChartService.getCountsByLoanStatus(inputDate,brand);
        }

       @PostConstruct
       public void initializeStartDate() {
        inputDate = LocalDateTime.now().minusDays(durationInDays);
    }

    }


