package com.efundzz.crmservice.controller;

import com.efundzz.crmservice.DTO.CRMLeadDataResponseDTO;
import com.efundzz.crmservice.service.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
