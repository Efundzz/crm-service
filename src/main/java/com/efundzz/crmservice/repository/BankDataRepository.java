package com.efundzz.crmservice.repository;


import com.efundzz.crmservice.entity.BankData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankDataRepository extends JpaRepository<BankData, Long> {
	Optional<BankData> findByBankNameAndCompanyCategory(Object object, String companyCategory);
}
