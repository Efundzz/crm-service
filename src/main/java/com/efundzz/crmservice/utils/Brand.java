package com.efundzz.crmservice.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Brand {

    public static List<String> determineReadAccess(List<String> permissions) {
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

    public  static List<String> determineCreateAccess(List<String> permissions) {
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
