package com.efundzz.crmservice.service;

import com.efundzz.crmservice.entity.Leads;
import com.efundzz.crmservice.repository.LeadRepository;
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

    public byte[] generateLoanReport(String brand) throws IOException, JRException {
        List<Leads> loans = leadRepository.findByBrand(brand);
        File file = ResourceUtils.getFile("classpath:loantemplate.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(loans);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ReportTitle", "Loan Report");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public List<Leads> getLeadsByBrand(String brand) {
        return leadRepository.findByBrand(brand);
    }

    public Workbook generateLeadsFormExcel(List<Leads> leadsList) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Leads");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"lead_id", "city", "created_dt", "pincode", "emailid", "mobile_number", "name", "loantype", "brand"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        for (int rowIndex = 0; rowIndex < leadsList.size(); rowIndex++) {
            Row dataRow = sheet.createRow(rowIndex + 1);
            Leads lead = leadsList.get(rowIndex);
            dataRow.createCell(0).setCellValue(lead.getId());
            dataRow.createCell(1).setCellValue(lead.getCity());
            dataRow.createCell(2).setCellValue(lead.getCreatedAt());
            dataRow.createCell(3).setCellValue(lead.getPincode());
            dataRow.createCell(4).setCellValue(lead.getEmailId());
            dataRow.createCell(5).setCellValue(lead.getMobileNumber());
            dataRow.createCell(6).setCellValue(lead.getName());
            dataRow.createCell(7).setCellValue(lead.getLoanType());
            dataRow.createCell(8).setCellValue(lead.getBrand());
        }
        return workbook;
    }

}