package com.efundzz.crmservice.repository;

import com.efundzz.crmservice.entity.CrmUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrmUserRepository extends JpaRepository<CrmUser, Long> {
    CrmUser findByUserName(String userName);
}
