package com.efundzz.crmservice.repository;

import com.efundzz.crmservice.entity.Leads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface LeadRepository extends JpaRepository<Leads, Serializable> {
    @Query("SELECT l FROM Leads l WHERE (:brand is null or l.brand = :brand)")
    List<Leads> findByBrand(String brand);

    Leads findById(Long id);

    @Query("SELECT l FROM Leads l " +
            "WHERE (:brand is null or l.brand = :brand) " +
            "AND (:loanType is null or l.loanType = :loanType) " +
            "AND (:name is null or l.name LIKE CONCAT('%', :name, '%')) " +
            "AND (:fromDate is null or DATE(l.createdAt) >= DATE(CAST(:fromDate AS date)))" +
            "AND (:toDate is null or DATE(l.createdAt)<= DATE(CAST(:toDate AS date)))" +
            "ORDER BY l.createdAt ASC")
    List<Leads> findLeadFormDataByFilter(
            @Param("brand") String brand,
            @Param("loanType") String loanType,
            @Param("name") String name,
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate);
}

