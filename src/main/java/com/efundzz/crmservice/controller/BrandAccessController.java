package com.efundzz.crmservice.controller;

import com.efundzz.crmservice.entity.Franchise;
import com.efundzz.crmservice.service.BrandAccessService;
import com.efundzz.crmservice.service.FranchiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

import static com.efundzz.crmservice.constants.AppConstants.*;

@RestController
@RequestMapping(path = "api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class BrandAccessController {

    @Autowired
    private BrandAccessService brandAccessService;

    @Autowired
    FranchiseService franchiseService;

    @GetMapping("/accessBrands")
    public ResponseEntity<List<String>> getReadBrands(JwtAuthenticationToken token) {
        String brand = determineBrandByToken(token);
        brand = EFUNDZZ_ORG.equals(brand) ? ALL_PERMISSION : brand;
        if (brand.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }
        return ResponseEntity.ok(Collections.singletonList(brand));
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

}