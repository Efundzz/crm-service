package com.efundzz.crmservice.controller;

import com.efundzz.crmservice.DTO.FranchiseDTO;
import com.efundzz.crmservice.entity.Franchise;
import com.efundzz.crmservice.service.FranchiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/franchises")
public class FranchiseController {

    private final FranchiseService franchiseService;

    @Autowired
    public FranchiseController(FranchiseService franchiseService) {
        this.franchiseService = franchiseService;
    }

    @PostMapping()
    public ResponseEntity<?> createFranchise(@Valid @RequestBody FranchiseDTO franchiseDTO) {
        try {
            Franchise franchise = franchiseService.createFranchise(franchiseDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(franchise);
        } catch (Exception e) {
            // Handle exceptions appropriately
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<?> listFranchises() {
        return ResponseEntity.ok(franchiseService.listFranchises());
    }

    @PutMapping()
    public ResponseEntity<?> addOrgUnit() {
        return ResponseEntity.ok(franchiseService.listFranchises());
    }
}
