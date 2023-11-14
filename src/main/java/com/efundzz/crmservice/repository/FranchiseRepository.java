package com.efundzz.crmservice.repository;

import com.efundzz.crmservice.entity.Franchise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FranchiseRepository extends JpaRepository<Franchise, Long> {
    boolean existsByFranchisePrefix(String prefix);

    boolean existsByName(String name);

    Franchise findFranchisePrefixByOrgId(String orgId);

}

