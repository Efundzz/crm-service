package com.efundzz.crmservice.controller;

import com.efundzz.crmservice.DTO.CRMAppliacationResponseDTO;
import com.efundzz.crmservice.DTO.CRMLeadFilterRequestDTO;
import com.efundzz.crmservice.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class ApplicationsController {

    @Autowired
    private LoanService loanService;

    @GetMapping("/applications")
    public ResponseEntity<List<CRMAppliacationResponseDTO>> getApplications() {
        // Get all applications from the database
//        List<String> permissions = token.getToken().getClaim("permissions");
//        String brand = determineBrand(permissions);
//        if (brand == null) {
//            throw new RuntimeException("Invalid permissions"); // Adjust error handling as needed.
//        }
//        System.out.println(permissions);
        return ResponseEntity.ok(loanService.getAllLoanDataWithMergedStepData("ALL"));
    }


    @GetMapping("/filteredApplications")
    public ResponseEntity<List<CRMAppliacationResponseDTO>> getApplicationsDataByFilter(@RequestBody CRMLeadFilterRequestDTO filterRequest){
        // Get all applications from the database
//        List<String> permissions = token.getToken().getClaim("permissions");
//        String brand = determineBrand(permissions);
//        if (brand == null) {
//            throw new RuntimeException("Invalid permissions"); // Adjust error handling as needed.
//        }
//        System.out.println(permissions);
        List<CRMAppliacationResponseDTO> filteredApplications = loanService.findApplicationsByFilter(
                filterRequest.getBrand(), filterRequest.getLoanType(),
                filterRequest.getFormDate(),
                filterRequest.getName(),
                filterRequest.getToDate());
        return ResponseEntity.ok(filteredApplications);
    }

}


