package com.yamankwefati.webshopapi.dao.order;

import com.yamankwefati.webshopapi.dao.email.EmailDAO;
import com.yamankwefati.webshopapi.dao.orderItem.OrderItemDAO;
import com.yamankwefati.webshopapi.dao.product.ProductDAO;
import com.yamankwefati.webshopapi.dao.user.UserRepository;
import com.yamankwefati.webshopapi.model.OrderItem;
import com.yamankwefati.webshopapi.model.Product;
import com.yamankwefati.webshopapi.model.ShopOrder;
import com.yamankwefati.webshopapi.model.User;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;

@Component
public class OrderDAO {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderItemDAO orderItemDAO;
    private final EmailDAO emailDAO;
    private final ProductDAO productDAO;

    public OrderDAO(OrderRepository orderRepository, UserRepository userRepository, OrderItemDAO orderItemDAO,
                    EmailDAO emailDAO, ProductDAO productDAO) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderItemDAO = orderItemDAO;
        this.emailDAO = emailDAO;
        this.productDAO = productDAO;
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
                .userId(shopOrder.getUserId())
                .totalAmount(shopOrder.getTotalAmount())
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

    public void sendOrderConfirmationEmail(ShopOrder order) {
        Map<String, Object> variables = new HashMap<>();
        List<OrderItem> orderItems = this.orderItemDAO.getOrderItemsByOrderId(order.getId());
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < orderItems.size(); i++) {
            Optional<Product> product = productDAO.getProductById((long) orderItems.get(i).getProductId());
            if (product.isEmpty()){
                return;
            }
            products.add(product.get());
        }
        variables.put("user", order.getUserId());
        variables.put("order", order);
        variables.put("orderItems", orderItems);
        variables.put("products", products);


        String content = this.emailDAO.buildOrderEmail("order-confirmation", variables);
        this.emailDAO.sendOrderConfirmationEmail(order.getUserId().getEmail(), content, "Order Confirmation");
    }

}
