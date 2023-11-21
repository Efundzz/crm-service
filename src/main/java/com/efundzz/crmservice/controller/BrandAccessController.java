package com.efundzz.crmservice.controller;

import com.efundzz.crmservice.service.BrandAccessService;
import com.efundzz.crmservice.service.BrandService;
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

import static com.efundzz.crmservice.constants.AppConstants.ALL_PERMISSION;
import static com.efundzz.crmservice.constants.AppConstants.EFUNDZZ_ORG;

@RestController
@RequestMapping(path = "api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class BrandAccessController {

    @Autowired
    private BrandAccessService brandAccessService;

    @Autowired
    FranchiseService franchiseService;

    @Autowired
    BrandService brandService;

    @GetMapping("/accessBrands")
    public ResponseEntity<List<String>> getAccessBrands(JwtAuthenticationToken token) {
        String brand = brandService.determineBrandByToken(token);
        brand = EFUNDZZ_ORG.equals(brand) ? ALL_PERMISSION : brand;
        if (brand.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }
        return ResponseEntity.ok(Collections.singletonList(brand));
    }
}