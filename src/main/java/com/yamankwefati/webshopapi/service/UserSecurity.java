package com.yamankwefati.webshopapi.service;

import com.yamankwefati.webshopapi.model.Role;
import com.yamankwefati.webshopapi.model.User;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Supplier;

@Component
public class UserSecurity implements AuthorizationManager<RequestAuthorizationContext> {
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        Long userId = Long.parseLong(object.getVariables().get("userId"));
        Authentication authentication1 = authentication.get();
        return new AuthorizationDecision(hasUserId(authentication1, userId));
    }

    public boolean hasUserId(Authentication authentication, Long userId) {
        Optional<User> userOptional = (Optional<User>) authentication.getPrincipal();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getId().equals(userId) || user.getUserRole().equals(Role.ADMIN);
        }
        return false;
    }
}
