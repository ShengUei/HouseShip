package com.grp4.houseship.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
public class InjectAuditor implements AuditorAware<String> {

    //CreatedBy, LastModifiedBy設定
    @Override
    public Optional<String> getCurrentAuditor() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext == null){
            return null;
        }
        if (securityContext.getAuthentication() == null){
            return null;
        }else {
            String userLoginName = securityContext.getAuthentication().getName();
            Optional<String> name = Optional.ofNullable(userLoginName);
            return name;
        }

    }
}
