package com.sirokuma.travelpro.repository;

import com.sirokuma.travelpro.entity.SocialLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialLoginRepository extends JpaRepository<SocialLogin, Long> {

}
