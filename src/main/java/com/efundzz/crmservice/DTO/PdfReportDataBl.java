package com.efundzz.crmservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PdfReportDataBl {
 private  String id;
  private  String loanType;
 private  String loanAmount;
 private  String purposeOfLoan;
 private  String loanTenure;
 private  String date;
 private  String loanSubType;
 private  String nameOfTheCard;
 private  String pan;
 private  String panHolderStatus;
 private  String mobile;
 private  String email;
 private  String businessName;
 private  String proprietorName;
 private  String gstNumber;
 private  String businessConstitution;
 private  String businessIndustry;
 private String  dateOfApplication;
 private  String  annualTurnover;
 private  String gstinUINStatus;
 private  String registrationDate;
 private  String partners;
 private  String addressAsPerGST;
 private  String currentBusinessAddress;
 private  String documents;
 private String cinNumber;
}
