package com.efundzz.crmservice.repository;

import com.efundzz.crmservice.DTO.CRMLoanDashBordResponceDTO;
import com.efundzz.crmservice.entity.Leads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DashBordChartRepository extends JpaRepository<Leads, Serializable> {
    @Query("SELECT l.loanType, COUNT(*) FROM Loan l WHERE l.createdAt >= :startDate AND l.brand = :brand GROUP BY l.loanType")
    List<Object[]> getCountsByLoanType(LocalDateTime startDate,String brand);

    @Query("SELECT l.status, COUNT(*) FROM Loan l WHERE l.createdAt >= :startDate AND l.brand = :brand GROUP BY l.status")
    List<Object[]> getCountsByLoanStatus(LocalDateTime startDate,String brand);

}
