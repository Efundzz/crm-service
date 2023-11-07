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
            "AND (:fromDate is null or DATE(l.createdAt) >= DATE(:fromDate)) " +
            "AND (:toDate is null or DATE(l.createdAt) <= DATE(:toDate)) " +
            "AND (:status is null or l.status = :status) " +
            "ORDER BY l.createdAt ASC")
    List<Leads> findLeadFormDataByFilter(
            @Param("brand") String brand,
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate,
            @Param("status") String status);



}

