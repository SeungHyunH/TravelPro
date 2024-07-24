package com.sirokuma.travelpro.service;

import com.sirokuma.travelpro.entity.SocialLogin;
import com.sirokuma.travelpro.repository.SocialLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialLoginService {
    @Autowired
    SocialLoginRepository socialLoginRepository;
    public void save(SocialLogin socialLogin) {
        socialLoginRepository.save(socialLogin);
    }
}
