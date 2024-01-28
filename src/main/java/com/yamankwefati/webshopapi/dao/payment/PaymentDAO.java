package com.yamankwefati.webshopapi.dao.payment;


import com.yamankwefati.webshopapi.dao.order.OrderDAO;
import com.yamankwefati.webshopapi.dao.orderItem.OrderItemDAO;
import com.yamankwefati.webshopapi.dao.product.ProductDAO;
import com.yamankwefati.webshopapi.dao.user.UserDAO;
import com.yamankwefati.webshopapi.model.*;
import javassist.NotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class PaymentDAO {

    private final OrderDAO orderDAO;
    private final UserDAO userDAO;
    private final OrderItemDAO orderItemDAO;
    private final ProductDAO productDAO;

    public PaymentDAO(OrderDAO orderDAO, UserDAO userDAO, OrderItemDAO orderItemDAO, ProductDAO productDAO) {
        this.orderDAO = orderDAO;
        this.userDAO = userDAO;
        this.orderItemDAO = orderItemDAO;
        this.productDAO = productDAO;
    }

    public void processSuccessfulPayment(long amountTotal, String customerEmail,
                                         List<CartItem> orderItems) throws NotFoundException {
        BigDecimal orderTotal = BigDecimal.valueOf(amountTotal);
        Optional<User> userOptional = this.userDAO.getUserByEmail(customerEmail);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            var order = ShopOrder.builder()
                    .userId(user)
                    .totalAmount(orderTotal.doubleValue())
                    .build();

            ShopOrder savedOrder = this.orderDAO.saveNewOrder(order);

            for (CartItem item : orderItems) {
                OrderItem orderItem = OrderItem.builder()
                        .shopOrderId(savedOrder)
                        .productId(item.getProductId())
                        .quantity((int) item.getQuantity())
                        .subtotal((double) item.getPrice())
                        .build();
                this.orderItemDAO.saveNewOrderItem(orderItem);
            }

            this.orderDAO.sendOrderConfirmationEmail(savedOrder);
        } else {
            System.out.println("User not found with email: " + customerEmail);
        }
    }

    public Long calculateOrderTotalAmount(List<CartItem> cartItems){
        long totalAmount = 0L;
        for (var item : cartItems) {
            Optional<Product> optionalProduct = this.productDAO.getProductById((long) item.getProductId());
            if (optionalProduct.isEmpty()) return -1L;
            Product product = optionalProduct.get();

            BigDecimal price = BigDecimal.valueOf(product.getPrice());
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            BigDecimal totalItemPrice = price.multiply(quantity);

            totalAmount += totalItemPrice.longValue();
        }

        return totalAmount;
    }
}
