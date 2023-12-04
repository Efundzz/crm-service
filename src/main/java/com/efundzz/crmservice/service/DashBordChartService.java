package com.efundzz.crmservice.service;

import com.efundzz.crmservice.dto.CRMLoanDashBordResponceDTO;
import com.efundzz.crmservice.mapper.CRMLoanDashBordResponceMapper;
import com.efundzz.crmservice.repository.DashBordChartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashBordChartService {
    @Autowired
    private DashBordChartRepository dashBordChartRepository;

    @Autowired
    private CRMLoanDashBordResponceMapper crmLoanDashBordResponceMapper;

    public List<CRMLoanDashBordResponceDTO> getCountsByLoanType(LocalDateTime inputDate, String brand) {
        List<Object[]> result;
        if (brand != null && brand.equalsIgnoreCase("ALL")) {
            result = dashBordChartRepository.getCountsByLoanType(inputDate, null);
        } else {
            result = dashBordChartRepository.getCountsByLoanType(inputDate, brand);
        }
        return crmLoanDashBordResponceMapper.mapToDTO(result);
    }

    public List<CRMLoanDashBordResponceDTO> getCountsByLoanStatus(LocalDateTime inputDate, String brand) {
        List<Object[]> result;
        if (brand != null && brand.equalsIgnoreCase("ALL")) {
            result = dashBordChartRepository.getCountsByLoanStatus(inputDate, null);
        } else {
            result = dashBordChartRepository.getCountsByLoanStatus(inputDate, brand);
        }
        return crmLoanDashBordResponceMapper.mapToDTO(result);
    }

    public List<CRMLoanDashBordResponceDTO> getLoanCountByBrand(LocalDateTime inputDate,String brand) {
        List<Object[]> result;
        if (brand != null && brand.equalsIgnoreCase("ALL")) {
            result = dashBordChartRepository.getLoansCountByBrand(inputDate,null);
        } else {
            result = dashBordChartRepository.getLoansCountByBrand(inputDate,brand);
        }
        return crmLoanDashBordResponceMapper.mapToDTO(result);
    }
}

