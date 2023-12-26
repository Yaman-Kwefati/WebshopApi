package com.yamankwefati.webshopapi.service;

import com.yamankwefati.webshopapi.dao.order.OrderRepository;
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
public class OrderSecurity implements AuthorizationManager<RequestAuthorizationContext> {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        Long orderId = Long.parseLong(object.getVariables().get("orderId"));
        Authentication authentication1 = authentication.get();
        return new AuthorizationDecision(hasUserId(authentication1, orderId));
    }

    public boolean hasUserId(Authentication authentication, Long orderId) {
        Optional<User> userOptional = (Optional<User>) authentication.getPrincipal();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Long orderUserId = getOrderUserId(orderId);
            return user.getId().equals(orderUserId) || user.getUserRole().equals(Role.ADMIN);
        }
        return false;
    }

    private Long getOrderUserId(Long orderId){
        return this.orderRepository.findById(orderId).get().getUserId().getId();
    }
}
