package com.efundzz.crmservice.service;

import com.efundzz.crmservice.entity.OrganizationUnit;
import com.efundzz.crmservice.repository.OrganizationUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrganizationService {

    private final OrganizationUnitRepository organizationUnitRepository;

    @Autowired
    public OrganizationService(OrganizationUnitRepository organizationUnitRepository) {
        this.organizationUnitRepository = organizationUnitRepository;
    }

    public OrganizationUnit createOrganizationUnit(String name, String type, OrganizationUnit parent) {
        OrganizationUnit unit = new OrganizationUnit();
        unit.setName(name);
        unit.setType(type);
        unit.setParent(parent);
        unit.setReferralId(UUID.randomUUID().toString()); // Generate unique referral ID

        return organizationUnitRepository.save(unit);
    }

    // Additional methods for updating, deleting, listing, and getting reports
}

