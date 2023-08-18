package com.efundzz.crmservice.service;

import com.efundzz.crmservice.DTO.CRMLoanDashBordResponceDTO;
import com.efundzz.crmservice.Mapper.CRMLoanDashBordResponceMapper;
import com.efundzz.crmservice.repository.DashBordChartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashBordChartService{
    @Value("${custom.duration.days}")
    private int durationInDays;
    @Autowired
    private DashBordChartRepository dashBordChartRepository;

    @Autowired
    private CRMLoanDashBordResponceMapper crmLoanDashBordResponceMapper;

    private LocalDateTime inputDate;

    @PostConstruct
    public void initializeStartDate() {
        inputDate = LocalDateTime.now().minusDays(durationInDays);
    }

    public List<CRMLoanDashBordResponceDTO> getLoanTypeCountsByDuration() {
        List<Object[]> result = dashBordChartRepository.getLoanTypeCountsByDuration(inputDate);
        return crmLoanDashBordResponceMapper.mapToDTO(result);
    }

    public List<CRMLoanDashBordResponceDTO> getLoanTypeCountsStatus() {
        List<Object[]> result = dashBordChartRepository.getLoanTypeCountsByStatus(inputDate);
        return crmLoanDashBordResponceMapper.mapToDTO(result);
    }

}

