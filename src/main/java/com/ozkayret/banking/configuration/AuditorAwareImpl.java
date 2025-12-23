package com.ozkayret.banking.configuration;


import com.ozkayret.banking.entity.UserDetailsImpl;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

public class AuditorAwareImpl implements AuditorAware<UUID> {

    @Override
    public Optional<UUID> getCurrentAuditor() {
        return Optional.of(getUserDetails());//.orElse(1L);
    }

    public UUID getUserDetails() {
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (obj != null && !"anonymousUser".equals(obj.toString())) {
            // TODO burayı yap
           // return UUID.fromString("00000000-0000-0000-0000-000000000000") ; //obj.getClass().get;
            return ((UserDetailsImpl) obj).getId();
        } else {
            return UUID.fromString("00000000-0000-0000-0000-000000000000");
        }
    }
}