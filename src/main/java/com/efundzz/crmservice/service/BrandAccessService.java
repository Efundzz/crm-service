package com.efundzz.crmservice.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BrandAccessService {
    public List<String> determineReadBrands(List<String> permissions) {
        Map<String, String> permissionsToBrands = new HashMap<>();
        permissionsToBrands.put("read:leads", "RD");
        permissionsToBrands.put("read:all_applications", "ALL");
        List<String> brands = new ArrayList<>();

        for (String permission : permissions) {
            if (permissionsToBrands.containsKey(permission)) {
                brands.add(permissionsToBrands.get(permission));
            }
        }
        return brands;
    }

    public List<String> determineWriteBrands(List<String> permissions) {
        Map<String, String> permissionsToBrands = new HashMap<>();
        permissionsToBrands.put("create:leads", "CR");
        permissionsToBrands.put("create:all_applications", "ALL");
        List<String> brands = new ArrayList<>();

        for (String permission : permissions) {
            if (permissionsToBrands.containsKey(permission)) {
                brands.add(permissionsToBrands.get(permission));
            }
        }
        return brands;
    }
}
