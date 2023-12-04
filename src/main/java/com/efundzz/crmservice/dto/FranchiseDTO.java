package com.efundzz.crmservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FranchiseDTO {
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Name must be lowercase and can include hyphens")
    private String name;

    private String displayName;

    @Length(max = 3, message = "Franchise prefix must be at most 3 characters long")
    @Pattern(regexp = "^[A-Z]{2,3}$", message = "Franchise prefix must be uppercase and 2-3 characters long")
    private String franchisePrefix;

    // ... other properties as needed with getters and setters
}
