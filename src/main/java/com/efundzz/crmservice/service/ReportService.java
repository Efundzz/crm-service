package com.efundzz.crmservice.service;

import com.efundzz.crmservice.DTO.CRMAppliacationResponseDTO;
import com.efundzz.crmservice.DTO.PdfReportBlDataDTO;
import com.efundzz.crmservice.DTO.PdfReportDataDTO;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReportService {
    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private LoanRepository loanRepository;
    public byte[] generateLoanReport(List<CRMAppliacationResponseDTO> leadData) throws IOException, JRException {
        if (leadData.get(0).getLoanType().equalsIgnoreCase("PersonalLoan")) {
            List<PdfReportDataDTO> reportDataList = mapLeadInfoToPdfData(leadData);
            File file = ResourceUtils.getFile("classpath:leadpdfdata.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportDataList);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ReportTitle", "Lead Report");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } else if (leadData.get(0).getLoanType().equalsIgnoreCase("BusinessLoan")) {
            List<PdfReportBlDataDTO> reportDataList = mapLeadInfoToPdfDataBl(leadData);
            File file = ResourceUtils.getFile("classpath:leadpdfdataBl.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportDataList);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ReportTitleBL", "Lead ReportBL");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }
        return null;
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
        String[] headers = {"RefNumber", "Application Date","Channel/SubChannel", "PIN Code", "Name", "Phone number", "Email id", "Loan Amount", "Type of Loans","City","Status"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        Map<String,String> channel=Map.of("EF","Efundzz", "RL", "Robo Loanz", "VH","Vahak", "BM" ,"Buy More", "HL", "Home Lane", "IV", "Invoyz", "KH", "Knowledge Hut", "WD", "Wedezine", "YH", "Yoho", "RB", "Raahi Bharat");
        for (int rowIndex = 0; rowIndex < leadsList.size(); rowIndex++) {
            Row dataRow = sheet.createRow(rowIndex + 1);
            Leads lead = leadsList.get(rowIndex);
            dataRow.createCell(0).setCellValue(lead.getId());
            dataRow.createCell(1).setCellValue(String.valueOf(lead.getCreatedAt()));
            String brand="";
            if(lead.getBrand()!=null)
                brand=lead.getBrand().toUpperCase();
            dataRow.createCell(2).setCellValue(String.valueOf(channel.containsKey(brand)?channel.get(brand):"NA"));
            dataRow.createCell(3).setCellValue(lead.getPincode());
            dataRow.createCell(4).setCellValue(lead.getName());
            dataRow.createCell(5).setCellValue(lead.getMobileNumber());
            dataRow.createCell(6).setCellValue(lead.getEmailId());
            Map<String, Object> data = lead.getAdditionalParams();
            if (data != null) {
                dataRow.createCell(7).setCellValue(data.containsKey("loanAmount") ? String.valueOf(data.get("loanAmount")) : "");
            } else {
                dataRow.createCell(7).setCellValue("");
            }
            dataRow.createCell(8).setCellValue(lead.getLoanType());
            dataRow.createCell(9).setCellValue(lead.getCity());
            dataRow.createCell(10).setCellValue(String.valueOf(lead.getStatus()));
        }
        return workbook;
    }
    public Workbook generateLeadsDataExcel(List<CRMAppliacationResponseDTO> leadsList, String loanType) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Leads");
        Row headerRow = sheet.createRow(0);
        String[] headers;
        if ("PersonalLoan".equals(loanType)) {
            headers = new String[]{"RefNumber", "Application Date", "PIN Code", "Name", "Phone number", "Email id", "Loan Amount", "Type of Loans", "Loan Status", "Pan","takeHomeSalaryMonthly", "purposeOfLoan", "employmentType", "dob", "adhaar", "gender", "currentAddress", "addressAsPerAdhaar", "residentType",
                    "referenceName", "referenceMobile", "referenceMobile"};
        } else if ("BusinessLoan".equals(loanType) || "StartupLoan".equals(loanType)) {
            headers = new String[]{"RefNumber", "Application Date", "PIN Code", "Name", "Phone number", "Email id", "Loan Amount", "Type of Loans", "Loan Status","Pan","businessIndustry", "gstNumber", "sameAsGst", "businessName", "annualTurnover", "proprietorName", "addressAsPerGST", "grossTotalIncome"
                    , "registrationDate", "currentBusinessAddress", "referenceMobile", "creditScore", "profitAfterTax", "yearsInBusiness","Pancard number","Purpose of loan"};
        } else if ("OneTapLoan".equalsIgnoreCase(loanType)) {
            headers=new String[]{"RefNumber", "Application Date", "PIN Code", "Name", "Phone number", "Email id", "Loan Amount", "Type of Loans", "Loan Status","Pan","aadhaar","DateOfBirth","Address"};

        } else {
            headers = new String[]{"RefNumber", "Application Date", "PIN Code", "Name", "Phone number", "Email id", "Loan Amount", "Type of Loans", "Loan Status","Pan","Address"};
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
            dataRow.createCell(8).setCellValue(String.valueOf(loan.getStatus()));
            dataRow.createCell(9).setCellValue(data.containsKey("pan") ? String.valueOf(data.get("pan")).toUpperCase(): "");

            if ("PersonalLoan".equals(loanType)) {
                dataRow.createCell(10).setCellValue(data.containsKey("takeHomeSalaryMonthly") ? String.valueOf(data.get("takeHomeSalaryMonthly")) : "");
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
            } else if ("BusinessLoan".equals(loanType) || "StartupLoan".equals(loanType)) {
                dataRow.createCell(10).setCellValue(data.containsKey("businessIndustry") ? String.valueOf(data.get("businessIndustry")) : "");
                dataRow.createCell(11).setCellValue(data.containsKey("gstNumber") ? String.valueOf(data.get("gstNumber")) : "");
                dataRow.createCell(12).setCellValue(data.containsKey("sameAsGst") ? String.valueOf(data.get("sameAsGst")) : "");
                dataRow.createCell(13).setCellValue(data.containsKey("businessName") ? String.valueOf(data.get("businessName")) : "");
                dataRow.createCell(14).setCellValue(data.containsKey("annualTurnover") ? String.valueOf(data.get("annualTurnover")) : "");
                dataRow.createCell(15).setCellValue(data.containsKey("proprietorName") ? String.valueOf(data.get("proprietorName")) : "");
                dataRow.createCell(16).setCellValue(data.containsKey("addressAsPerGST") ? String.valueOf(data.get("addressAsPerGST")) : "");
                dataRow.createCell(17).setCellValue(data.containsKey("grossTotalIncome") ? String.valueOf(data.get("grossTotalIncome")) : "");
                dataRow.createCell(18).setCellValue(data.containsKey("registrationDate") ? String.valueOf(data.get("registrationDate")) : "");
                dataRow.createCell(19).setCellValue(data.containsKey("currentBusinessAddress") ? String.valueOf(data.get("currentBusinessAddress")) : "");
                dataRow.createCell(20).setCellValue(data.containsKey("referenceMobile") ? String.valueOf(data.get("referenceMobile")) : "");
                dataRow.createCell(21).setCellValue(data.containsKey("creditScore") ? String.valueOf(data.get("creditScore")) : "");
                dataRow.createCell(22).setCellValue(data.containsKey("profitAfterTax") ? String.valueOf(data.get("profitAfterTax")) : "");
                dataRow.createCell(23).setCellValue(data.containsKey("yearsInBusiness") ? String.valueOf(data.get("yearsInBusiness")) : "");
                dataRow.createCell(24).setCellValue(data.containsKey("purposeOfLoan") ? String.valueOf(data.get("purposeOfLoan")) : "");

            }
            else if("OneTapLoan".equalsIgnoreCase(loanType)){
                dataRow.createCell(2).setCellValue(data.containsKey("pincode") ? String.valueOf(data.get("pincode")) : "");
                dataRow.createCell(10).setCellValue(data.containsKey("aadhar") ? String.valueOf(data.get("aadhar")) : "");
                dataRow.createCell(11).setCellValue(data.containsKey("dob") ? String.valueOf(data.get("dob")) : "");
                dataRow.createCell(12).setCellValue(data.containsKey("address") ? String.valueOf(data.get("address")) : "");
                dataRow.createCell(7).setCellValue(loanType);
            }
            else {
                dataRow.createCell(10).setCellValue(data.containsKey("currentAddress") ? String.valueOf(data.get("currentAddress")) : "");
            }
        }
        return workbook;
    }

    public Workbook generateSingleLeadDataExcel(List<CRMAppliacationResponseDTO> leadsList, String loanType) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Leads");
        Row headerRow = sheet.createRow(0);
        String[] headers;
        Map<String,String> channel=Map.of("EF","Efundzz", "RL", "Robo Loanz", "VH","Vahak", "BM" ,"Buy More", "HL", "Home Lane", "IV", "Invoyz", "KH", "Knowledge Hut", "WD", "Wedezine", "YH", "Yoho", "RB", "Raahi Bharat");
        if ("PersonalLoan".equals(loanType)) {
            headers = new String[]{"RefNumber", "Application Date", "PIN Code", "Name", "Phone number", "Email id", "Loan Amount", "Type of Loans", "Loan Status", "Pan","takeHomeSalaryMonthly", "purposeOfLoan", "employmentType", "dob", "adhaar", "gender", "currentAddress", "addressAsPerAdhaar", "residentType",
                    "referenceName", "referenceMobile"};
        } else if ("BusinessLoan".equals(loanType) || "StartupLoan".equals(loanType)) {
            headers = new String[]{"RefNumber", "Application Date", "PIN Code", "Name", "Phone number", "Email id", "Loan Amount", "Type of Loans", "Loan Status","Pan","businessIndustry", "gstNumber", "sameAsGst", "businessName", "annualTurnover", "proprietorName", "addressAsPerGST", "grossTotalIncome"
                    , "registrationDate", "currentBusinessAddress", "referenceMobile", "creditScore", "profitAfterTax", "yearsInBusiness","Pancard number","Purpose of loan"};
        } else {
            headers = new String[]{"RefNumber", "Application Date", "PIN Code", "Name", "Phone number", "Email id", "Loan Amount", "Type of Loans", "Loan Status","Pan","Address"};
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
            dataRow.createCell(9).setCellValue(data.containsKey("pan") ? String.valueOf(data.get("pan")).toUpperCase() : "");
            if ("PersonalLoan".equals(loanType)) {
                dataRow.createCell(10).setCellValue(data.containsKey("takeHomeSalaryMonthly") ? String.valueOf(data.get("takeHomeSalaryMonthly")) : "");
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
            } else if ("BusinessLoan".equals(loanType) || "StartupLoan".equals(loanType)) {
                dataRow.createCell(10).setCellValue(data.containsKey("businessIndustry") ? String.valueOf(data.get("businessIndustry")) : "");
                dataRow.createCell(11).setCellValue(data.containsKey("gstNumber") ? String.valueOf(data.get("gstNumber")) : "");
                dataRow.createCell(12).setCellValue(data.containsKey("sameAsGst") ? String.valueOf(data.get("sameAsGst")) : "");
                dataRow.createCell(13).setCellValue(data.containsKey("businessName") ? String.valueOf(data.get("businessName")) : "");
                dataRow.createCell(14).setCellValue(data.containsKey("annualTurnover") ? String.valueOf(data.get("annualTurnover")) : "");
                dataRow.createCell(15).setCellValue(data.containsKey("proprietorName") ? String.valueOf(data.get("proprietorName")) : "");
                dataRow.createCell(16).setCellValue(data.containsKey("addressAsPerGST") ? String.valueOf(data.get("addressAsPerGST")) : "");
                dataRow.createCell(17).setCellValue(data.containsKey("grossTotalIncome") ? String.valueOf(data.get("grossTotalIncome")) : "");
                dataRow.createCell(18).setCellValue(data.containsKey("registrationDate") ? String.valueOf(data.get("registrationDate")) : "");
                dataRow.createCell(19).setCellValue(data.containsKey("currentBusinessAddress") ? String.valueOf(data.get("currentBusinessAddress")) : "");
                dataRow.createCell(20).setCellValue(data.containsKey("referenceMobile") ? String.valueOf(data.get("referenceMobile")) : "");
                dataRow.createCell(21).setCellValue(data.containsKey("creditScore") ? String.valueOf(data.get("creditScore")) : "");
                dataRow.createCell(22).setCellValue(data.containsKey("profitAfterTax") ? String.valueOf(data.get("profitAfterTax")) : "");
                dataRow.createCell(23).setCellValue(data.containsKey("yearsInBusiness") ? String.valueOf(data.get("yearsInBusiness")) : "");
                dataRow.createCell(24).setCellValue(data.containsKey("purposeOfLoan") ? String.valueOf(data.get("purposeOfLoan")) : "");
            } else {
                dataRow.createCell(10).setCellValue(data.containsKey("currentAddress") ? String.valueOf(data.get("currentAddress")) : "");
            }
        }
        return workbook;
    }

    public Workbook generateSingleLeadFormExcel(Leads lead) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Lead");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"RefNumber", "Application Date", "PIN Code", "Name", "Phone number", "Email id", "Loan Amount", "Type of Loans","Brand","City","Status"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(lead.getId());
        dataRow.createCell(1).setCellValue(String.valueOf(lead.getCreatedAt()));
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
        dataRow.createCell(8).setCellValue(lead.getBrand());
        dataRow.createCell(9).setCellValue(lead.getCity());
        dataRow.createCell(10).setCellValue(lead.getStatus());
        return workbook;
    }
    public List<PdfReportDataDTO> mapLeadInfoToPdfData(List<CRMAppliacationResponseDTO> leadData) {
        List<PdfReportDataDTO> reportDataList = new ArrayList<>();
        for (CRMAppliacationResponseDTO lead : leadData) {

            PdfReportDataDTO reportData = new PdfReportDataDTO();
            reportData.setId(lead.getId());
            reportData.setLoanType(lead.getLoanType());
            String address=(lead.getData().containsKey("addressAsPerAdhaar")?String.valueOf(lead.getData().get("addressAsPerAdhaar")):null);
            Map<String, Object> leadDataMap = lead.getData();
            String docType ="";
            if (leadDataMap.containsKey("documents")) {
                List<Map<String, Object>> documents = (List<Map<String, Object>>)leadDataMap.get("documents");
                for (Map<String, Object> document : documents) {
                    docType=docType+"\n"+(String) document.get("docType")+":"+document.get("docTitle");
                }
            }
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String  date = currentDate.format(formatter);
            reportData.setDate(date);// setting date of application
            reportData.setDocument1(docType);
            // reportData.setDocument1((lead.getData().containsKey("documents")?(Map<String,String>)list.get(0):null));
            reportData.setPurposeOfLoan(lead.getData().containsKey("purposeOfLoan") ? String.valueOf(lead.getData().get("purposeOfLoan")) : null);
            reportData.setLoanAmount(lead.getData().containsKey("loanAmount") ? String.valueOf(lead.getData().get("loanAmount")) : null);
            reportData.setLoanType(lead.getData().containsKey("loanType") ? String.valueOf(lead.getData().get("loanType")) : null);
            reportData.setTotalEmiPaying(lead.getData().containsKey("totalEmi") ? String.valueOf(lead.getData().get("totalEmi")) : null);
            reportData.setDateOfApplication(lead.getData().containsKey("date") ? String.valueOf(lead.getData().get("date")) : null);
            reportData.setCreditScore(lead.getData().containsKey("creditScore") ? String.valueOf(lead.getData().get("creditScore")) : null);
            reportData.setNameOnCard(lead.getData().containsKey("nameOfTheCard") ? String.valueOf(lead.getData().get("nameOfTheCard")) : null);
            reportData.setPanCardNumber(lead.getData().containsKey("pan") ? String.valueOf(lead.getData().get("pan")).toUpperCase().toUpperCase() : null);
            reportData.setLastUpdate(lead.getData().containsKey("lastUpdate") ? String.valueOf(lead.getData().get("lastUpdate")) : null);
            reportData.setPinCodeAadhaar(lead.getData().containsKey("addressAsPerAdhaar") ? String.valueOf(lead.getData().get("addressAsPerAdhaar")).replace("{","").replace("}","").replace("=",":"): null);
            reportData.setStatus(lead.getData().containsKey("status") ? String.valueOf(lead.getData().get("status")) : null);
            reportData.setDescription(lead.getData().containsKey("description") ? String.valueOf(lead.getData().get("description")) : null);
            reportData.setDigitallySignedOn(lead.getData().containsKey("panVerified") ? String.valueOf(lead.getData().get("panVerified")) : null);
            reportData.setName(lead.getData().containsKey("fullName") ? String.valueOf(lead.getData().get("fullName")) : null);
            reportData.setDownloadDate(lead.getData().containsKey("downloadDate") ? String.valueOf(lead.getData().get("downloadDate")) : null);
            reportData.setFatherName(lead.getData().containsKey("fatherName") ? String.valueOf(lead.getData().get("fatherName")) : null);
            reportData.setAadhaarNumber(lead.getData().containsKey("adhaar") ? String.valueOf(lead.getData().get("adhaar")) : null);
            reportData.setGeneratedDate(lead.getData().containsKey("generatedDate") ? String.valueOf(lead.getData().get("generatedDate")) : null);
            reportData.setDateOfBirth(lead.getData().containsKey("dob") ? String.valueOf(lead.getData().get("dob")) : null);
            reportData.setGender(lead.getData().containsKey("gender") ? String.valueOf(lead.getData().get("gender")) : null);
            reportData.setHouseNumber(lead.getData().containsKey("addressLine") ? String.valueOf(lead.getData().get("houseNumber")) : null);
            reportData.setStreet(lead.getData().containsKey("street") ? String.valueOf(lead.getData().get("street")) : null);
            reportData.setLocality(lead.getData().containsKey("addressLine") ? String.valueOf(lead.getData().get("locality")) : null);
            reportData.setLandmark(lead.getData().containsKey("landmark") ? String.valueOf(lead.getData().get("landmark")) : null);
            reportData.setPostOffice(lead.getData().containsKey("pincode") ? String.valueOf(lead.getData().get("pincode")) : null);
            reportData.setVillageAadhaar(lead.getData().containsKey("city") ? String.valueOf(lead.getData().get("city")) : null);
            reportData.setStateAadhaar(lead.getData().containsKey("state") ? String.valueOf(lead.getData().get("stateAadhaar")) : null);
            reportData.setDistrictAadhaar(lead.getData().containsKey("district") ? String.valueOf(lead.getData().get("districtAadhaar")) : null);
            reportData.setSubDistrict(lead.getData().containsKey("district") ? String.valueOf(lead.getData().get("subDistrict")) : null);
            reportData.setCountry(lead.getData().containsKey("country") ? String.valueOf(lead.getData().get("country")) : null);
            reportData.setAddress(lead.getData().containsKey("address") ? String.valueOf(lead.getData().get("address")) : null);
            reportData.setSignature(lead.getData().containsKey("signature") ? String.valueOf(lead.getData().get("signature")) : null);
            reportData.setMobileNumber(lead.getData().containsKey("mobile") ? String.valueOf(lead.getData().get("mobile")) : null);
            reportData.setAlternateMobileNumber(lead.getData().containsKey("alternateMobileNumber") ? String.valueOf(lead.getData().get("alternateMobileNumber")) : "NA");
            reportData.setEmailId(lead.getData().containsKey("email") ? String.valueOf(lead.getData().get("email")) : null);
            reportData.setResidentType(lead.getData().containsKey("residentType") ? String.valueOf(lead.getData().get("residentType")) : null);
            reportData.setMonthlyRent(lead.getData().containsKey("rent") ? String.valueOf(lead.getData().get("rent")) : null);
            reportData.setYearsOfCurrentResident(lead.getData().containsKey("yearsAtCurrentResident") ? String.valueOf(lead.getData().get("yearsAtCurrentResident")) : null);
            reportData.setNoOfDependants(lead.getData().containsKey("numberOfDependents") ? String.valueOf(lead.getData().get("numberOfDependents")) : null);
            reportData.setFlatNoStreetName(lead.getData().containsKey("addressLine") ? String.valueOf(lead.getData().get("flatNoStreetName")) : null);
            reportData.setVillage(lead.getData().containsKey("village") ? String.valueOf(lead.getData().get("village")) : null);
            reportData.setDistrict(lead.getData().containsKey("district") ? String.valueOf(lead.getData().get("district")) : null);
            reportData.setState(lead.getData().containsKey("state") ? String.valueOf(lead.getData().get("state")) : null);
            reportData.setPinCode(lead.getData().containsKey("currentAddress") ? String.valueOf(lead.getData().get("currentAddress")).replace("{","").replace("}","").replace("=",":") : null);
            reportData.setEmployeeType(lead.getData().containsKey("employmentType") ? String.valueOf(lead.getData().get("employmentType")) : null);
            reportData.setCurrentOrganization(lead.getData().containsKey("currentOrganization") ? String.valueOf(lead.getData().get("currentOrganization")) : null);
            reportData.setTotalWorkExperience(lead.getData().containsKey("totalExperience") ? String.valueOf(lead.getData().get("totalExperience")) : null);
            reportData.setCurrentJobStability(lead.getData().containsKey("currentDesignation") ? String.valueOf(lead.getData().get("currentDesignation")) :null);
            reportData.setSalary(lead.getData().containsKey("takeHomeSalaryMonthly") ? String.valueOf(lead.getData().get("takeHomeSalaryMonthly")) : null);
            reportData.setPersonName(lead.getData().containsKey("referenceName") ? String.valueOf(lead.getData().get("referenceName")) : null);
            reportData.setReferenceMobileNumber(lead.getData().containsKey("referenceMobile") ? String.valueOf(lead.getData().get("referenceMobile")) : null);
            reportDataList.add(reportData);
        }
        return reportDataList;
    }

    public List<PdfReportBlDataDTO> mapLeadInfoToPdfDataBl(List<CRMAppliacationResponseDTO> leadData) {
        List<PdfReportBlDataDTO> reportDataList = new ArrayList<>();
        for (CRMAppliacationResponseDTO lead : leadData) {
            PdfReportBlDataDTO pdfReportDataBl=new PdfReportBlDataDTO();
            Map<String, Object> leadDataMap = lead.getData();
            String docType ="";
            if (leadDataMap.containsKey("documents")) {
                List<Map<String, Object>> documents = (List<Map<String, Object>>)leadDataMap.get("documents");
                for (Map<String, Object> document : documents) {
                    docType=docType+"\n"+(String) document.get("docType")+":"+document.get("docTitle");
                }
            }
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String  date = currentDate.format(formatter);
            pdfReportDataBl.setDateOfApplication(date);
            pdfReportDataBl.setId(lead.getId());
            pdfReportDataBl.setDocuments(docType.equalsIgnoreCase("")?"Not Uploaded Any Documents":docType);
            pdfReportDataBl.setLoanType(String.valueOf(lead.getLoanType()));
            pdfReportDataBl.setLoanAmount(lead.getData().containsKey("loanAmount") ? String.valueOf(lead.getData().get("loanAmount")) : null);
            pdfReportDataBl.setPurposeOfLoan(lead.getData().containsKey("purposeOfLoan") ? String.valueOf(lead.getData().get("purposeOfLoan")) : null);
            pdfReportDataBl.setLoanTenure(lead.getData().containsKey("loanTenure") ? String.valueOf(lead.getData().get("loanTenure")) : null);
            pdfReportDataBl.setDate(lead.getData().containsKey("date") ? String.valueOf(lead.getData().get("date")) : null);
            pdfReportDataBl.setLoanSubType(lead.getLoanSubType());
            pdfReportDataBl.setNameOfTheCard(lead.getData().containsKey("nameOfTheCard") ? String.valueOf(lead.getData().get("nameOfTheCard")) : null);
            pdfReportDataBl.setPan(lead.getData().containsKey("pan") ? String.valueOf(lead.getData().get("pan")).toUpperCase() : null);
            pdfReportDataBl.setPanHolderStatus(lead.getData().containsKey("panHolderStatus") ? String.valueOf(lead.getData().get("panHolderStatus")) : null);
            pdfReportDataBl.setMobile(lead.getData().containsKey("mobile") ? String.valueOf(lead.getData().get("mobile")) : null);
            pdfReportDataBl.setEmail(lead.getData().containsKey("email") ? String.valueOf(lead.getData().get("email")) : null);
            pdfReportDataBl.setBusinessName(lead.getData().containsKey("businessName") ? String.valueOf(lead.getData().get("businessName")) : null);
            pdfReportDataBl.setProprietorName(lead.getData().containsKey("proprietorName") ? String.valueOf(lead.getData().get("proprietorName")) : null);
            pdfReportDataBl.setGstNumber(lead.getData().containsKey("gstNumber") ? String.valueOf(lead.getData().get("gstNumber")) : null);
            pdfReportDataBl.setBusinessConstitution(lead.getData().containsKey("businessConstitution") ? String.valueOf(lead.getData().get("businessConstitution")) : null);
            pdfReportDataBl.setBusinessIndustry(lead.getData().containsKey("businessIndustry") ? String.valueOf(lead.getData().get("businessIndustry")) : null);
            pdfReportDataBl.setAnnualTurnover(lead.getData().containsKey("annualTurnover") ? String.valueOf(lead.getData().get("annualTurnover")) : null);
            pdfReportDataBl.setGstinUINStatus(lead.getData().containsKey("gstinUINStatus") ? String.valueOf(lead.getData().get("gstinUINStatus")) : null);
            pdfReportDataBl.setRegistrationDate(lead.getData().containsKey("registrationDate") ? String.valueOf(lead.getData().get("registrationDate")) : null);
            pdfReportDataBl.setPartners(lead.getData().containsKey("partners") ? (String.valueOf(lead.getData().get("partners"))) .equalsIgnoreCase("null")?"NA":String.valueOf(lead.getData().get("partners")).replace("{","Partner : ").replace("}","\n\n") .replace("=",":").replace("[","").replace("]",""): "NA");
            pdfReportDataBl.setAddressAsPerGST(lead.getData().containsKey("addressAsPerGST") ? String.valueOf(lead.getData().get("addressAsPerGST")) : null);
            pdfReportDataBl.setCurrentBusinessAddress(lead.getData().containsKey("currentBusinessAddress") ? String.valueOf(lead.getData().get("currentBusinessAddress")) : null);
            pdfReportDataBl.setCinNumber(lead.getData().containsKey("CinNumber")?(String.valueOf(lead.getData().get("CinNumber"))).equalsIgnoreCase("null")?"NA":(String.valueOf(lead.getData().get("CinNumber"))):"NA");
            reportDataList.add(pdfReportDataBl);
        }
        return reportDataList;
    }
}