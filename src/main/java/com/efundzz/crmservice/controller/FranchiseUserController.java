package com.efundzz.crmservice.controller;

import com.efundzz.crmservice.dto.CrmUserRequestDTO;
import com.efundzz.crmservice.entity.CrmUser;
import com.efundzz.crmservice.entity.FranchiseData;
import com.efundzz.crmservice.service.FranchiseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class FranchiseUserController {

    @Autowired
    private FranchiseUserService userService;

    @PostMapping("/register")
    public ResponseEntity<CrmUser> registerUser(@RequestBody CrmUserRequestDTO userRequestDTO) {
        CrmUser user = CrmUser.builder()
                .userName(userRequestDTO.getUserName())
                .organizationName(userRequestDTO.getOrganizationName())
                .emailId(userRequestDTO.getEmailId())
                .franchiseData(FranchiseData.builder().id(userRequestDTO.getFranchiseId()).build())
                .build();

        CrmUser registeredUser = userService.registerUser(user, userRequestDTO.getPassword());
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

}
