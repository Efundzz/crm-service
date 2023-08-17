package com.efundzz.crmservice.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Brand {

    public static String determineBrand(List<String> permissions) {
        Map<String, String> permissionsToBrands = new HashMap<>();
        permissionsToBrands.put("read:efundzz_applications", "EF");
        permissionsToBrands.put("read:roboloanz_applications", "RL");
        permissionsToBrands.put("read:vahak_leads", "VH");
        permissionsToBrands.put("read:all_applications", "ALL");
        // You can enhance this logic based on your requirements.
        for (String permission : permissions) {
            if (permissionsToBrands.containsKey(permission)) {
                return permissionsToBrands.get(permission);
            }
        }

        return null;
    }
}
