package com.yamankwefati.webshopapi.dao.order;

import com.yamankwefati.webshopapi.dao.user.UserRepository;
import com.yamankwefati.webshopapi.model.ShopOrder;
import com.yamankwefati.webshopapi.model.User;
import javassist.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class OrderDAO {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderDAO(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public List<ShopOrder> getAllOrders(){
        return this.orderRepository.findAll();
    }

    public Optional<ShopOrder> getOrderById(Long orderId){
        return this.orderRepository.findById(orderId);
    }

    public List<ShopOrder> getAllUserOrders(String email) {
        Optional<User> user = this.userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return Collections.emptyList();
        }
        User user1 = user.get();
        return this.orderRepository.getOrdersByUserId(user1);
    }

    public ShopOrder saveNewOrder(ShopOrder shopOrder){
        var order = ShopOrder.builder()
                .orderDate(shopOrder.getOrderDate())
                .totalAmount(shopOrder.getTotalAmount())
                .userId(shopOrder.getUserId())
                .build();
        return this.orderRepository.save(order);
    }

    public ShopOrder updateOrderStatus(ShopOrder newOrder, Long orderId) throws NotFoundException {
        Optional<ShopOrder> oldOrder = this.orderRepository.findById(orderId);
        if (oldOrder.isEmpty()){
            throw new NotFoundException("User with id: " + orderId + " not found");
        }
        ShopOrder order = oldOrder.get();
        order.setOrderStatus(newOrder.getOrderStatus());
        return this.orderRepository.save(order);
    }
}
