package com.efundzz.crmservice.service;

import com.efundzz.crmservice.DTO.CRMAppliacationResponseDTO;
import com.efundzz.crmservice.entity.Leads;
import com.efundzz.crmservice.repository.LeadRepository;
import com.efundzz.crmservice.repository.LoanRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private LoanRepository loanRepository;

    public byte[] generateLoanReport(String brand) throws IOException, JRException {
        List<Leads> loans = leadRepository.findByBrand(brand);
        File file = ResourceUtils.getFile("classpath:leadsReportTemplate.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(loans);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ReportTitle", "Lead Report");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public List<Leads> getLeadsByBrand(String brand) {
        if ("ALL".equalsIgnoreCase(brand)) {
            return leadRepository.findByBrand(null);
        } else {
            return leadRepository.findByBrand(brand);
        }
    }

    public Workbook generateLeadsFormExcel(List<Leads> leadsList) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Leads");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"RefNumber", "Application Date", "PIN Code", "Name", "Phone number", "Email id", "Loan Amount", "Type of Loans"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        for (int rowIndex = 0; rowIndex < leadsList.size(); rowIndex++) {
            Row dataRow = sheet.createRow(rowIndex + 1);
            Leads lead = leadsList.get(rowIndex);
            dataRow.createCell(0).setCellValue(lead.getId());
            dataRow.createCell(1).setCellValue(lead.getCreatedAt());
            dataRow.createCell(2).setCellValue(lead.getPincode());
            dataRow.createCell(3).setCellValue(lead.getName());
            dataRow.createCell(4).setCellValue(lead.getMobileNumber());
            dataRow.createCell(5).setCellValue(lead.getEmailId());
            Map<String, Object> data = lead.getAdditionalParams();
            if (data != null) {
                dataRow.createCell(6).setCellValue(data.containsKey("loanAmount") ? String.valueOf(data.get("loanAmount")) : "");
            } else {
                dataRow.createCell(6).setCellValue("");
            }
            dataRow.createCell(7).setCellValue(lead.getLoanType());
        }
        return workbook;
    }

    public Workbook generateLeadsDataExcel(List<CRMAppliacationResponseDTO> leadsList, String loanType) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Leads");
        Row headerRow = sheet.createRow(0);
        String[] headers;
        if ("PersonalLoan".equals(loanType)) {
            headers = new String[]{"RefNumber", "Application Date", "PIN Code", "Name", "Phone number", "Email id", "Loan Amount", "Type of Loans", "Loan Status", "takeHomeSalaryMonthly", "pan", "purposeOfLoan", "employmentType", "dob", "adhaar", "gender", "currentAddress", "addressAsPerAdhaar", "residentType",
                    "referenceName", "referenceMobile", "referenceMobile"};
        } else if ("BusinessLoan".equals(loanType)) {
            headers = new String[]{"RefNumber", "Application Date", "PIN Code", "Name", "Phone number", "Email id", "Loan Amount", "Type of Loans", "Loan Status", "businessIndustry", "gstNumber", "sameAsGst", "businessName", "annualTurnover", "proprietorName", "addressAsPerGST", "grossTotalIncome"
                    , "registrationDate", "currentBusinessAddress", "referenceMobile", "creditScore", "profitAfterTax", "yearsInBusiness"};
        } else {
            headers = new String[]{"RefNumber", "Application Date", "PIN Code", "Name", "Phone number", "Email id", "Loan Amount", "Type of Loans", "Loan Status","Address"};
        }
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        for (int rowIndex = 0; rowIndex < leadsList.size(); rowIndex++) {
            Row dataRow = sheet.createRow(rowIndex + 1);
            CRMAppliacationResponseDTO loan = leadsList.get(rowIndex);
            dataRow.createCell(0).setCellValue(loan.getId());
            Map<String, Object> data = loan.getData();
            dataRow.createCell(1).setCellValue(data.containsKey("date") ? String.valueOf(data.get("date")) : "");
            dataRow.createCell(2).setCellValue(data.containsKey("pin") ? String.valueOf(data.get("pin")) : "");
            dataRow.createCell(3).setCellValue(data.containsKey("fullName") ? String.valueOf(data.get("fullName")) : "");
            dataRow.createCell(4).setCellValue(data.containsKey("mobile") ? String.valueOf(data.get("mobile")) : "");
            dataRow.createCell(5).setCellValue(data.containsKey("email") ? String.valueOf(data.get("email")) : "");
            dataRow.createCell(6).setCellValue(data.containsKey("loanAmount") ? String.valueOf(data.get("loanAmount")) : "");
            dataRow.createCell(7).setCellValue(data.containsKey("loanType") ? String.valueOf(data.get("loanType")) : "");
            dataRow.createCell(8).setCellValue(loan.getStatus());
            if ("PersonalLoan".equals(loanType)) {
                dataRow.createCell(9).setCellValue(data.containsKey("takeHomeSalaryMonthly") ? String.valueOf(data.get("takeHomeSalaryMonthly")) : "");
                dataRow.createCell(10).setCellValue(data.containsKey("pan") ? String.valueOf(data.get("pan")) : "");
                dataRow.createCell(11).setCellValue(data.containsKey("purposeOfLoan") ? String.valueOf(data.get("purposeOfLoan")) : "");
                dataRow.createCell(12).setCellValue(data.containsKey("employmentType") ? String.valueOf(data.get("employmentType")) : "");
                dataRow.createCell(13).setCellValue(data.containsKey("dob") ? String.valueOf(data.get("dob")) : "");
                dataRow.createCell(14).setCellValue(data.containsKey("adhaar") ? String.valueOf(data.get("adhaar")) : "");
                dataRow.createCell(15).setCellValue(data.containsKey("gender") ? String.valueOf(data.get("gender")) : "");
                dataRow.createCell(16).setCellValue(data.containsKey("currentAddress") ? String.valueOf(data.get("currentAddress")) : "");
                dataRow.createCell(17).setCellValue(data.containsKey("addressAsPerAdhaar") ? String.valueOf(data.get("addressAsPerAdhaar")) : "");
                dataRow.createCell(18).setCellValue(data.containsKey("residentType") ? String.valueOf(data.get("residentType")) : "");
                dataRow.createCell(19).setCellValue(data.containsKey("referenceName") ? String.valueOf(data.get("referenceName")) : "");
                dataRow.createCell(20).setCellValue(data.containsKey("referenceMobile") ? String.valueOf(data.get("referenceMobile")) : "");
            } else if ("BusinessLoan".equals(loanType)) {
                dataRow.createCell(9).setCellValue(data.containsKey("businessIndustry") ? String.valueOf(data.get("businessIndustry")) : "");
                dataRow.createCell(10).setCellValue(data.containsKey("gstNumber") ? String.valueOf(data.get("gstNumber")) : "");
                dataRow.createCell(11).setCellValue(data.containsKey("sameAsGst") ? String.valueOf(data.get("sameAsGst")) : "");
                dataRow.createCell(12).setCellValue(data.containsKey("businessName") ? String.valueOf(data.get("businessName")) : "");
                dataRow.createCell(13).setCellValue(data.containsKey("annualTurnover") ? String.valueOf(data.get("annualTurnover")) : "");
                dataRow.createCell(14).setCellValue(data.containsKey("proprietorName") ? String.valueOf(data.get("proprietorName")) : "");
                dataRow.createCell(15).setCellValue(data.containsKey("addressAsPerGST") ? String.valueOf(data.get("addressAsPerGST")) : "");
                dataRow.createCell(16).setCellValue(data.containsKey("grossTotalIncome") ? String.valueOf(data.get("grossTotalIncome")) : "");
                dataRow.createCell(17).setCellValue(data.containsKey("registrationDate") ? String.valueOf(data.get("registrationDate")) : "");
                dataRow.createCell(18).setCellValue(data.containsKey("currentBusinessAddress") ? String.valueOf(data.get("currentBusinessAddress")) : "");
                dataRow.createCell(19).setCellValue(data.containsKey("referenceMobile") ? String.valueOf(data.get("referenceMobile")) : "");
                dataRow.createCell(20).setCellValue(data.containsKey("creditScore") ? String.valueOf(data.get("creditScore")) : "");
                dataRow.createCell(21).setCellValue(data.containsKey("profitAfterTax") ? String.valueOf(data.get("profitAfterTax")) : "");
                dataRow.createCell(22).setCellValue(data.containsKey("yearsInBusiness") ? String.valueOf(data.get("yearsInBusiness")) : "");
            } else {
                dataRow.createCell(9).setCellValue(data.containsKey("currentAddress") ? String.valueOf(data.get("currentAddress")) : "");
            }

        }
        return workbook;
    }

    public Workbook generateSingleLeadDataExcel(List<CRMAppliacationResponseDTO> leadsList, String loanType) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Leads");
        Row headerRow = sheet.createRow(0);
        String[] headers;
        if ("PersonalLoan".equals(loanType)) {
            headers = new String[]{"RefNumber", "Application Date", "PIN Code", "Name", "Phone number", "Email id", "Loan Amount", "Type of Loans", "Loan Status", "takeHomeSalaryMonthly", "pan", "purposeOfLoan", "employmentType", "dob", "adhaar", "gender", "currentAddress", "addressAsPerAdhaar", "residentType",
                    "referenceName", "referenceMobile", "referenceMobile"};
        } else if ("BusinessLoan".equals(loanType)) {
            headers = new String[]{"RefNumber", "Application Date", "PIN Code", "Name", "Phone number", "Email id", "Loan Amount", "Type of Loans", "Loan Status", "businessIndustry", "gstNumber", "sameAsGst", "businessName", "annualTurnover", "proprietorName", "addressAsPerGST", "grossTotalIncome"
                    , "registrationDate", "currentBusinessAddress", "referenceMobile", "creditScore", "profitAfterTax", "yearsInBusiness"};
        } else {
            headers = new String[]{"RefNumber", "Application Date", "PIN Code", "Name", "Phone number", "Email id", "Loan Amount", "Type of Loans", "Loan Status"};
        }
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        for (int rowIndex = 0; rowIndex < leadsList.size(); rowIndex++) {
            Row dataRow = sheet.createRow(rowIndex + 1);
            CRMAppliacationResponseDTO loan = leadsList.get(rowIndex);
            dataRow.createCell(0).setCellValue(loan.getId());
            Map<String, Object> data = loan.getData();
            dataRow.createCell(1).setCellValue(data.containsKey("date") ? String.valueOf(data.get("date")) : "");
            dataRow.createCell(2).setCellValue(data.containsKey("pin") ? String.valueOf(data.get("pin")) : "");
            dataRow.createCell(3).setCellValue(data.containsKey("fullName") ? String.valueOf(data.get("fullName")) : "");
            dataRow.createCell(4).setCellValue(data.containsKey("mobile") ? String.valueOf(data.get("mobile")) : "");
            dataRow.createCell(5).setCellValue(data.containsKey("email") ? String.valueOf(data.get("email")) : "");
            dataRow.createCell(6).setCellValue(data.containsKey("loanAmount") ? String.valueOf(data.get("loanAmount")) : "");
            dataRow.createCell(7).setCellValue(data.containsKey("loanType") ? String.valueOf(data.get("loanType")) : "");
            dataRow.createCell(8).setCellValue(loan.getStatus());
            if ("PersonalLoan".equals(loanType)) {
                dataRow.createCell(9).setCellValue(data.containsKey("takeHomeSalaryMonthly") ? String.valueOf(data.get("takeHomeSalaryMonthly")) : "");
                dataRow.createCell(10).setCellValue(data.containsKey("pan") ? String.valueOf(data.get("pan")) : "");
                dataRow.createCell(11).setCellValue(data.containsKey("purposeOfLoan") ? String.valueOf(data.get("purposeOfLoan")) : "");
                dataRow.createCell(12).setCellValue(data.containsKey("employmentType") ? String.valueOf(data.get("employmentType")) : "");
                dataRow.createCell(13).setCellValue(data.containsKey("dob") ? String.valueOf(data.get("dob")) : "");
                dataRow.createCell(14).setCellValue(data.containsKey("adhaar") ? String.valueOf(data.get("adhaar")) : "");
                dataRow.createCell(15).setCellValue(data.containsKey("gender") ? String.valueOf(data.get("gender")) : "");
                dataRow.createCell(16).setCellValue(data.containsKey("currentAddress") ? String.valueOf(data.get("currentAddress")) : "");
                dataRow.createCell(17).setCellValue(data.containsKey("addressAsPerAdhaar") ? String.valueOf(data.get("addressAsPerAdhaar")) : "");
                dataRow.createCell(18).setCellValue(data.containsKey("residentType") ? String.valueOf(data.get("residentType")) : "");
                dataRow.createCell(19).setCellValue(data.containsKey("referenceName") ? String.valueOf(data.get("referenceName")) : "");
                dataRow.createCell(20).setCellValue(data.containsKey("referenceMobile") ? String.valueOf(data.get("referenceMobile")) : "");
            } else if ("BusinessLoan".equals(loanType)) {
                dataRow.createCell(9).setCellValue(data.containsKey("businessIndustry") ? String.valueOf(data.get("businessIndustry")) : "");
                dataRow.createCell(10).setCellValue(data.containsKey("gstNumber") ? String.valueOf(data.get("gstNumber")) : "");
                dataRow.createCell(11).setCellValue(data.containsKey("sameAsGst") ? String.valueOf(data.get("sameAsGst")) : "");
                dataRow.createCell(12).setCellValue(data.containsKey("businessName") ? String.valueOf(data.get("businessName")) : "");
                dataRow.createCell(13).setCellValue(data.containsKey("annualTurnover") ? String.valueOf(data.get("annualTurnover")) : "");
                dataRow.createCell(14).setCellValue(data.containsKey("proprietorName") ? String.valueOf(data.get("proprietorName")) : "");
                dataRow.createCell(15).setCellValue(data.containsKey("addressAsPerGST") ? String.valueOf(data.get("addressAsPerGST")) : "");
                dataRow.createCell(16).setCellValue(data.containsKey("grossTotalIncome") ? String.valueOf(data.get("grossTotalIncome")) : "");
                dataRow.createCell(17).setCellValue(data.containsKey("registrationDate") ? String.valueOf(data.get("registrationDate")) : "");
                dataRow.createCell(18).setCellValue(data.containsKey("currentBusinessAddress") ? String.valueOf(data.get("currentBusinessAddress")) : "");
                dataRow.createCell(19).setCellValue(data.containsKey("referenceMobile") ? String.valueOf(data.get("referenceMobile")) : "");
                dataRow.createCell(20).setCellValue(data.containsKey("creditScore") ? String.valueOf(data.get("creditScore")) : "");
                dataRow.createCell(21).setCellValue(data.containsKey("profitAfterTax") ? String.valueOf(data.get("profitAfterTax")) : "");
                dataRow.createCell(22).setCellValue(data.containsKey("yearsInBusiness") ? String.valueOf(data.get("yearsInBusiness")) : "");
            } else {
                dataRow.createCell(9).setCellValue(data.containsKey("currentAddress") ? String.valueOf(data.get("currentAddress")) : "");
            }
        }
        return workbook;
    }

    public Workbook generateSingleLeadFormExcel(Leads lead) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Lead");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"RefNumber", "Application Date", "PIN Code", "Name", "Phone number", "Email id", "Loan Amount", "Type of Loans"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(lead.getId());
        dataRow.createCell(1).setCellValue(lead.getCreatedAt());
        dataRow.createCell(2).setCellValue(lead.getPincode());
        dataRow.createCell(3).setCellValue(lead.getName());
        dataRow.createCell(4).setCellValue(lead.getMobileNumber());
        dataRow.createCell(5).setCellValue(lead.getEmailId());
        Map<String, Object> data = lead.getAdditionalParams();
        if (data != null) {
            dataRow.createCell(6).setCellValue(data.containsKey("loanAmount") ? String.valueOf(data.get("loanAmount")) : "");
        } else {
            dataRow.createCell(6).setCellValue("");
        }
        dataRow.createCell(7).setCellValue(lead.getLoanType());

        return workbook;
    }

}