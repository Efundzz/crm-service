package com.efundzz.crmservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PdfReportDataDTO {
    private String id;
    private String loanType;
    private String purposeOfLoan;
    private String loanAmount;
    private String totalEmiPaying;
    private String dateOfApplication;
    private String creditScore;
    private String nameOnCard;
    private String panCardNumber;
    private String lastUpdate;
    private String pinCodeAadhaar;
    private String status;
    private String description;
    private String digitallySignedOn;
    private String name;
    private String downloadDate;
    private String fatherName;
    private String aadhaarNumber;
    private String generatedDate;
    private String dateOfBirth;
    private String gender;
    private String houseNumber;
    private String street;
    private String locality;
    private String landmark;
    private String postOffice;
    private String villageAadhaar;
    private String stateAadhaar;
    private String districtAadhaar;
    private String subDistrict;
    private String country;
    private String address;
    private String signature;
    private String mobileNumber;
    private String alternateMobileNumber;
    private String emailId;
    private String residentType;
    private String monthlyRent;
    private String yearsOfCurrentResident;
    private String noOfDependants;
    private String flatNoStreetName;
    private String village;
    private String district;
    private String state;
    private String pinCode;
    private String employeeType;
    private String currentOrganization;
    private String totalWorkExperience;
    private String currentJobStability;
    private String salary;
    private String personName;
    private String referenceMobileNumber;
    private String document1;
    private String document2;
    private String document3;
    private String document4;

}
