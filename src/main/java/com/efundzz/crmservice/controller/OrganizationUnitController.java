package com.efundzz.crmservice.controller;

import com.efundzz.crmservice.entity.OrganizationUnit;
import com.efundzz.crmservice.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/franchises/{franchiseId}/organization-units")
public class OrganizationUnitController {

    // private final OrganizationService organizationService;
   // private final AuthenticationFacade authenticationFacade;

//    @Autowired
//    public OrganizationUnitController(OrganizationService organizationService, AuthenticationFacade authenticationFacade) {
//        this.organizationService = organizationService;
//        this.authenticationFacade = authenticationFacade;
//    }
//
//    @PutMapping
//    public ResponseEntity<?> addOrgUnit(@PathVariable Long franchiseId, @RequestBody OrganizationUnitDTO organizationUnitDTO) {
//        if (!authenticationFacade.hasAdminPrivileges(franchiseId)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized to add organization units to this franchise");
//        }
//
//        OrganizationUnit newUnit = organizationService.addOrganizationUnitToFranchise(franchiseId, organizationUnitDTO);
//        return ResponseEntity.ok(newUnit);
//    }
}

