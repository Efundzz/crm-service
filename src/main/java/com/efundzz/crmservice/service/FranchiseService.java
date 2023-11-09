package com.efundzz.crmservice.service;

import com.efundzz.crmservice.DTO.FranchiseDTO;
import com.efundzz.crmservice.entity.Franchise;
import com.efundzz.crmservice.entity.OrganizationUnit;
import com.efundzz.crmservice.repository.FranchiseRepository;
import com.efundzz.crmservice.repository.OrganizationUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class FranchiseService {

    private final OrganizationUnitRepository organizationUnitRepository;

    private final FranchiseRepository franchiseRepository;
    private final Auth0ManagementService auth0ManagementService;

    @Autowired
    public FranchiseService(OrganizationUnitRepository organizationUnitRepository,
                            Auth0ManagementService auth0ManagementService,
                            FranchiseRepository franchiseRepository) {
        this.organizationUnitRepository = organizationUnitRepository;
        this.auth0ManagementService = auth0ManagementService;
        this.franchiseRepository = franchiseRepository;
    }

    @Transactional
    public Franchise createFranchise(FranchiseDTO franchiseDTO) {

        if (franchiseRepository.existsByName(franchiseDTO.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name must be unique");
        }

        String auth0OrgId = auth0ManagementService.createOrganization(franchiseDTO.getName(), franchiseDTO.getName(), franchiseDTO.getFranchisePrefix());

        OrganizationUnit franchiseUnit = OrganizationUnit.builder()
                .name(franchiseDTO.getName())
                .type("FRANCHISE")
                .referralId(UUID.randomUUID().toString())
                .build();

        // Persist the organization unit in the database
        franchiseUnit = organizationUnitRepository.save(franchiseUnit);

        // Now create the Franchise entity
        Franchise franchise = Franchise.builder()
                .name(franchiseDTO.getName())
                .franchisePrefix(franchiseDTO.getFranchisePrefix())
                .orgId(auth0OrgId)
                .organizationUnit(franchiseUnit)
                .build();

        // This save operation assumes you have a FranchiseRepository similar to OrganizationUnitRepository
        return franchiseRepository.save(franchise);
    }

    public List<Franchise> listFranchises(){
        return franchiseRepository.findAll();
    }
}
