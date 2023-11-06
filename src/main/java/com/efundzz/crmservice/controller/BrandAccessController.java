package com.efundzz.crmservice.controller;

import com.efundzz.crmservice.service.BrandAccessService;
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

import static com.efundzz.crmservice.constants.AppConstants.PERMISSIONS;

@RestController
@RequestMapping(path = "api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class BrandAccessController {

    @Autowired
    private BrandAccessService brandAccessService;
    @GetMapping("/readBrands")
    public ResponseEntity<List<String>> getReadBrands(JwtAuthenticationToken token) {
        List<String> permissions = token.getToken().getClaim(PERMISSIONS);
        List<String> brands = brandAccessService.determineReadBrands(permissions);

        if (brands.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }

        return ResponseEntity.ok(brands);
    }
    @GetMapping("/writeBrands")
    public ResponseEntity<List<String>> getWriteBrands(JwtAuthenticationToken token) {
        List<String> permissions = token.getToken().getClaim(PERMISSIONS);
        List<String> brands = brandAccessService.determineWriteBrands(permissions);

        if (brands.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }
        return ResponseEntity.ok(brands);
    }
}
