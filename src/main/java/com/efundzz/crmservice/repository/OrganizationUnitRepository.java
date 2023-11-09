package com.efundzz.crmservice.repository;

import com.efundzz.crmservice.entity.OrganizationUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationUnitRepository extends JpaRepository<OrganizationUnit, Long> {
    List<OrganizationUnit> findByTypeAndParent(String type, OrganizationUnit parent);
}

