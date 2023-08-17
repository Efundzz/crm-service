package com.efundzz.crmservice.repository;

import com.efundzz.crmservice.entity.Leads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface LeadRepository extends JpaRepository<Leads, Serializable> {
    List<Leads> findByBrand(String brand);

}
