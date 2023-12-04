package com.efundzz.crmservice.service;


import com.efundzz.crmservice.entity.CrmLogin;
import com.efundzz.crmservice.entity.CrmUser;
import com.efundzz.crmservice.entity.FranchiseData;
import com.efundzz.crmservice.repository.CrmUserRepository;
import com.efundzz.crmservice.repository.FranchiseDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class FranchiseUserService {

    @Autowired
    private CrmUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private FranchiseDataRepository franchiseDataRepository;

    public CrmUser registerUser(CrmUser user, String password) {
        CrmLogin login = CrmLogin.builder()
                .userName(user.getUserName())
                .passwordHash(passwordEncoder.encode(password))
                .user(user)
                .build();
        user.setLoginDetails(login);
        FranchiseData franchiseData = franchiseDataRepository.findById(user.getFranchiseData().getId())
                .orElseGet(() -> franchiseDataRepository.save(user.getFranchiseData()));
        user.setFranchiseData(franchiseData);
        userRepository.save(user);
        return user;
    }
}
