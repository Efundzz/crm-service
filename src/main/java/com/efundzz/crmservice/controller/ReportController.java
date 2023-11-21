package com.efundzz.crmservice.controller;

import com.efundzz.crmservice.DTO.CRMAppliacationResponseDTO;
import com.efundzz.crmservice.DTO.CRMLeadFilterRequestDTO;
import com.efundzz.crmservice.entity.Leads;
import com.efundzz.crmservice.service.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static com.efundzz.crmservice.constants.AppConstants.ALL_PERMISSION;
import static com.efundzz.crmservice.constants.AppConstants.EFUNDZZ_ORG;

@RestController
@RequestMapping(path = "api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class ReportController {
    @Autowired
    private ReportService reportService;
    @Autowired
    LoanService loanService;
    @Autowired
    LeadService leadService;

    @Autowired
    FranchiseService franchiseService;

    @Autowired
    BrandService brandService;

    @Value("${excel.contentType}")
    private String excelContentType;

    @GetMapping("/apps/download-pdf/{id}")
    public ResponseEntity<byte[]> generateLoanReport(JwtAuthenticationToken token, @PathVariable String id) {
        String brand = brandService.determineBrandByToken(token);
        try {
            List<CRMAppliacationResponseDTO> leadData = loanService.getAllLeadDataByAppId(id, brand);
            byte[] reportBytes = reportService.generateLoanReport(leadData);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "loan-report.pdf");
            return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/apps/bulk/download-excel")
    public ResponseEntity<Resource> exportLeadsToExcel(JwtAuthenticationToken token, @RequestBody CRMLeadFilterRequestDTO filterRequest) throws IOException {
        String brand = brandService.determineBrandByToken(token);
        String permitBrand = EFUNDZZ_ORG.equals(brand) ? ALL_PERMISSION : brand;
        String accessibleBrand = null;
        accessibleBrand = brandService.determineAccessibleBrand(brand, permitBrand, filterRequest.getBrand());
        List<CRMAppliacationResponseDTO> leadsList = loanService.findApplicationsByFilter(accessibleBrand,
                filterRequest.getLoanType(),
                filterRequest.getFromDate(),
                filterRequest.getToDate(),
                filterRequest.getLoanStatus());

        Workbook workbook = reportService.generateLeadsDataExcel(leadsList, filterRequest.getLoanType());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=AllApplicationsData.xlsx");

        InputStreamResource inputStreamResource = new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(excelContentType))
                .body(inputStreamResource);
    }

    @PostMapping("/leadForms/bulk/download-excel")
    public ResponseEntity<Resource> exportLeadsFormToExcel(JwtAuthenticationToken token, @RequestBody CRMLeadFilterRequestDTO filterRequest) throws IOException {
        String brand = brandService.determineBrandByToken(token);
        String permitBrand = EFUNDZZ_ORG.equals(brand) ? ALL_PERMISSION : brand;
        String accessibleBrand = null;
        accessibleBrand = brandService.determineAccessibleBrand(brand, permitBrand, filterRequest.getBrand());
        List<Leads> leadsList = leadService.findLeadFormDataByFilter(accessibleBrand,
                filterRequest.getLoanType(),
                filterRequest.getFromDate(),
                filterRequest.getToDate(),
                filterRequest.getStatus());
        Workbook workbook = reportService.generateLeadsFormExcel(leadsList);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=AllLeadsFormData.xlsx");

        InputStreamResource inputStreamResource = new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(excelContentType))
                .body(inputStreamResource);
    }

    @GetMapping("/leadForms/single/download-excel/{id}")
    public ResponseEntity<Resource> exportSingleLeadsFormToExcel(JwtAuthenticationToken token, @PathVariable Long id) throws IOException {
        Leads leaddata = leadService.getLeadFormDataById(id);
        Workbook workbook = reportService.generateSingleLeadFormExcel(leaddata);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=SingleLeadFormData.xlsx");

        InputStreamResource inputStreamResource = new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(excelContentType))
                .body(inputStreamResource);
    }

    @GetMapping("/apps/single/download-excel/{appId}")
    public ResponseEntity<Resource> exportSingleLeadToExcel(JwtAuthenticationToken token, @PathVariable String appId) throws IOException {
        String brand = brandService.determineBrandByToken(token);
        List<CRMAppliacationResponseDTO> leadData = loanService.getAllLeadDataByAppId(appId, brand);
        String loanType = !leadData.isEmpty() ? leadData.get(0).getLoanType() : "";
        Workbook workbook = reportService.generateSingleLeadDataExcel(leadData, loanType);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=SingleAppData.xlsx");

        InputStreamResource inputStreamResource = new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(excelContentType))
                .body(inputStreamResource);
    }
}
