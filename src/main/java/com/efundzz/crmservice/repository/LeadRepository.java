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
    List<Leads> findByBrand(String brand);

    @Query("SELECT l FROM Leads l " +
            "WHERE (:brand is null or l.brand = :brand) " +
            "AND (:loanType is null or l.loanType = :loanType) " +
            "AND (:name is null or l.name LIKE CONCAT('%', :name, '%')) " +
            "AND (:fromDate is null or l.createdAt >= CAST(:fromDate AS date)) " +
            "AND (:toDate is null or l.createdAt <= CAST(:toDate AS date))")

    List<Leads> findLeadFormDataByFilter(
            @Param("brand") String brand,
            @Param("loanType") String loanType,
            @Param("name") String name,
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate);
}

