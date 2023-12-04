package com.efundzz.crmservice.repository;

import com.efundzz.crmservice.entity.FranchiseData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FranchiseDataRepository extends JpaRepository<FranchiseData, Long> {
    Optional<FranchiseData> findById(Long id);
}