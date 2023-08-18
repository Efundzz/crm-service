package com.efundzz.crmservice.controller;

import com.efundzz.crmservice.DTO.CRMLoanDashBordResponceDTO;
import com.efundzz.crmservice.service.DashBordChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class DashBoardChartController {
        @Autowired
        private DashBordChartService dashBordChartService;

        @GetMapping("/leads/overview")
        public List<CRMLoanDashBordResponceDTO> getLeadsOverview() {
            return dashBordChartService.getLoanTypeCountsByDuration();
        }
        @GetMapping("/leads/byStatus")
        public List<CRMLoanDashBordResponceDTO> getLeadsByStatus() {
            return dashBordChartService.getLoanTypeCountsStatus();
        }

    }


