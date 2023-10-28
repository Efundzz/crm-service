package com.efundzz.crmservice.repository;

import com.efundzz.crmservice.entity.LeadsLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface LeadsLogRepository extends JpaRepository<LeadsLog, Serializable> {
    @Query("SELECT l FROM LeadsLog l WHERE (:leadId is null or l.leadId = :leadId)")
    List<LeadsLog> findByLeadId(Long leadId);
}
