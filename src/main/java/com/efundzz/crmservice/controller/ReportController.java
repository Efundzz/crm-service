package com.efundzz.crmservice.controller;

import com.efundzz.crmservice.DTO.CRMAppliacationResponseDTO;
import com.efundzz.crmservice.entity.Leads;
import com.efundzz.crmservice.service.LeadService;
import com.efundzz.crmservice.service.LoanService;
import com.efundzz.crmservice.service.ReportService;
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

import static com.efundzz.crmservice.utils.Brand.determineBrand;

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
    @Value("${excel.contentType}")
    private String excelContentType;

    @GetMapping("/leads/download-pdf")
    public ResponseEntity<byte[]> generateLoanReport(JwtAuthenticationToken token) {
        List<String> permissions = token.getToken().getClaim("permissions");
        String brand = determineBrand(permissions);
        if (brand == null) {
            throw new RuntimeException("Invalid permissions");
        }
        try {
            byte[] reportBytes = reportService.generateLoanReport(brand);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "loan-report.pdf");
            return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/bulkLead/download-excel")
    public ResponseEntity<Resource> exportLeadsToExcel() throws IOException {
//        List<String> permissions = token.getToken().getClaim("permissions");
//        String brand = determineBrand(permissions);
//        if (brand == null) {
//            throw new RuntimeException("Invalid permissions");
//        }
        List<CRMAppliacationResponseDTO> leadsList = loanService.getAllLoanDataWithMergedStepData("ALL");

        Workbook workbook = reportService.generateLeadsDataExcel(leadsList);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=leadsData.xlsx");

        InputStreamResource inputStreamResource = new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(excelContentType))
                .body(inputStreamResource);
    }

    @GetMapping("/bulkLeadForm/download-excel")
    public ResponseEntity<Resource> exportLeadsFormToExcel(JwtAuthenticationToken token) throws IOException {
        List<String> permissions = token.getToken().getClaim("permissions");
        String brand = determineBrand(permissions);
        if (brand == null) {
            throw new RuntimeException("Invalid permissions");
        }
        List<Leads> leadsList = reportService.getLeadsByBrand(brand);
        Workbook workbook = reportService.generateLeadsFormExcel(leadsList);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=leadsFormData.xlsx");

        InputStreamResource inputStreamResource = new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(excelContentType))
                .body(inputStreamResource);
    }

    @GetMapping("/leadForm/download-excel/{id}")
    public ResponseEntity<Resource> exportSingleLeadsFormToExcel(JwtAuthenticationToken token, @PathVariable Long id) throws IOException {

        List<String> permissions = token.getToken().getClaim("permissions");
        String brand = determineBrand(permissions);
        if (brand == null) {
            throw new RuntimeException("Invalid permissions");
        }
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

    @GetMapping("/leadData/download-excel/{appId}")
    public ResponseEntity<Resource> exportSingleLeadToExcel(JwtAuthenticationToken token, @PathVariable String appId) throws IOException {
        List<String> permissions = token.getToken().getClaim("permissions");
        String brand = determineBrand(permissions);
        if (brand == null) {
            throw new RuntimeException("Invalid permissions");
        }
        List<CRMAppliacationResponseDTO> leadData = leadService.getAllLeadDataByAppId(appId, brand);

        Workbook workbook = reportService.generateSingleLeadDataExcel(leadData);
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
}
