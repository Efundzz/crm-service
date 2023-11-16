package com.efundzz.crmservice.service;

import com.efundzz.crmservice.entity.Franchise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import static com.efundzz.crmservice.constants.AppConstants.ALL_PERMISSION;
import static com.efundzz.crmservice.constants.AppConstants.ORG_ID;

@Service
public class BrandService {

    private  FranchiseService franchiseService;

    @Autowired
    public void setFranchiseService(FranchiseService service) {
        franchiseService = service;
    }
    public String determineBrandByToken(JwtAuthenticationToken token) {
        String orgId = token.getToken().getClaim(ORG_ID);
        String brand = determineBrandByOrgId(orgId);
        if (brand == null) {
            throw new RuntimeException("Unauthorized access");
        }
        return brand;
    }

    public String determineAccessibleBrand(String brand, String permit, String filterBrand) {
        return permit.equals(ALL_PERMISSION) && filterBrand.equals(ALL_PERMISSION)
                ? ALL_PERMISSION
                : permit.equals(ALL_PERMISSION)
                ? filterBrand
                : brand;
    }

    private String determineBrandByOrgId(String orgId) {
        Franchise franchise = franchiseService.getFranchisePrefixByOrgId("org_7ve249G78zoYMLKs");
        return (franchise != null) ? franchise.getFranchisePrefix() : null;
    }

}
