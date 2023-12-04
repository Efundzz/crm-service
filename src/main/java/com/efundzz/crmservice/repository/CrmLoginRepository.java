package com.efundzz.crmservice.repository;

import com.efundzz.crmservice.entity.CrmLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrmLoginRepository extends JpaRepository<CrmLogin, Long> {
    CrmLogin findByUserName(String userName);
}