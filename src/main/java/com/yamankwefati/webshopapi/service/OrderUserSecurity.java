package com.yamankwefati.webshopapi.service;

import com.yamankwefati.webshopapi.dao.user.UserRepository;
import com.yamankwefati.webshopapi.model.Role;
import com.yamankwefati.webshopapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Supplier;

@Component
public class OrderUserSecurity implements AuthorizationManager<RequestAuthorizationContext> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        String orderEmail = String.valueOf(object.getVariables().get("email"));
        Authentication authentication1 = authentication.get();
        return new AuthorizationDecision(hasUserId(authentication1, orderEmail));
    }

    public boolean hasUserId(Authentication authentication, String orderEmail) {
        User user = (User) authentication.getPrincipal();
        if (user != null) {
            Long orderUserId = getOrderUserId(orderEmail);
            return user.getId().equals(orderUserId) || user.getUserRole().equals(Role.ADMIN);
        }
        return false;
    }

    private Long getOrderUserId(String orderEmail){
        return this.userRepository.findByEmail(orderEmail).get().getId();
    }
}
