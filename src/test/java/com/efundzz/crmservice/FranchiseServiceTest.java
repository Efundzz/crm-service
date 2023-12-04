package com.efundzz.crmservice;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.efundzz.crmservice.dto.FranchiseDTO;
import com.efundzz.crmservice.entity.Franchise;
import com.efundzz.crmservice.entity.OrganizationUnit;
import com.efundzz.crmservice.repository.OrganizationUnitRepository;
import com.efundzz.crmservice.service.Auth0ManagementService;
import com.efundzz.crmservice.service.FranchiseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class FranchiseServiceTest {

    @Mock
    private OrganizationUnitRepository organizationUnitRepository;

    @Mock
    private Auth0ManagementService auth0ManagementService;

    @InjectMocks
    private FranchiseService franchiseService;

    @Test
    void testCreateFranchise() {
        FranchiseDTO dto = new FranchiseDTO("test-franchise", "TEST","TE");
        String expectedAuth0OrgId = "auth0OrgId123";

        when(auth0ManagementService.createOrganization(anyString(), anyString(), anyString()))
                .thenReturn(expectedAuth0OrgId);

        when(organizationUnitRepository.save(any(OrganizationUnit.class)))
                .then(invocation -> invocation.getArgument(0));

        Franchise createdFranchise = franchiseService.createFranchise(dto);

        assertNotNull(createdFranchise);
        assertEquals(expectedAuth0OrgId, createdFranchise.getOrgId());
        verify(auth0ManagementService).createOrganization(dto.getName(), dto.getName(), dto.getFranchisePrefix());
        verify(organizationUnitRepository).save(any(OrganizationUnit.class));
    }
}

