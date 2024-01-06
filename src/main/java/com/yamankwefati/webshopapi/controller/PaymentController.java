package com.yamankwefati.webshopapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.StripeException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.yamankwefati.webshopapi.dao.order.OrderDAO;
import com.yamankwefati.webshopapi.dao.orderItem.OrderItemDAO;
import com.yamankwefati.webshopapi.dao.product.ProductDAO;
import com.yamankwefati.webshopapi.dao.user.UserDAO;
import com.yamankwefati.webshopapi.model.*;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value = "/api/v1/payment")
public class PaymentController {
    private final OrderDAO orderDAO;
    private final UserDAO userDAO;
    private final OrderItemDAO orderItemDAO;
    private final ProductDAO productDAO;

    public PaymentController(OrderDAO orderDAO, UserDAO userDAO, OrderItemDAO orderItemDAO, ProductDAO productDAO) {
        this.orderDAO = orderDAO;
        this.userDAO = userDAO;
        this.orderItemDAO = orderItemDAO;
        this.productDAO = productDAO;
    }

    @PostMapping("/create-checkout-session")
    public ApiResponse<String> createCheckoutSession(@RequestBody PaymentRequest paymentRequest) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String cartItemsJson = objectMapper.writeValueAsString(paymentRequest.getItems());
            SessionCreateParams.Builder sessionBuilder = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.IDEAL)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                    .putMetadata("cartItems", cartItemsJson)
//                .setSuccessUrl("http://159.223.236.93/success")
//                .setCancelUrl("http://159.223.236.93/cancel");
                .setSuccessUrl("https://e0b9-84-25-165-69.ngrok-free.app/success")
                .setCancelUrl("https://e0b9-84-25-165-69.ngrok-free.app/cancel");


            for (CartItem item : paymentRequest.getItems()) {
                sessionBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(item.getQuantity())
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("eur")
                                            .setUnitAmount((long) item.getPrice())
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName(item.getProductName())
                                                            .build()
                                            )
                                            .build())
                            .build()
                );
            }

            Session session = Session.create(sessionBuilder.build());
            System.out.println("Session Builder: " + session);
            return new ApiResponse<>(HttpStatus.OK, session.getUrl());

        } catch (StripeException e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/webhook")
    public ApiResponse<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(payload);

            JsonNode sessionNode = rootNode.path("data").path("object");
            long amountTotal = sessionNode.path("amount_total").asLong();
            String customerEmail = sessionNode.path("customer_details").path("email").asText();
            String cartItemsJson = sessionNode.path("metadata").path("cartItems").asText();

            List<CartItem> cartItems = objectMapper.readValue(cartItemsJson, new TypeReference<List<CartItem>>() {});

            if (amountTotal > 0 && !customerEmail.isEmpty()) {
                processSuccessfulPayment(amountTotal, customerEmail, cartItems);
            }

            return new ApiResponse<>(HttpStatus.OK, "OK");
        } catch (Exception e) {
            System.out.println("Error in handleStripeWebhook: " + e.getMessage());
            e.printStackTrace();
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, "Webhook Error: " + e.getMessage());
        }
    }


    private void processSuccessfulPayment(long amountTotal, String customerEmail,
                                          List<CartItem> orderItems) throws NotFoundException {
        double orderTotal = amountTotal / 100.0;
        Optional<User> userOptional = this.userDAO.getUserByEmail(customerEmail);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("User: " + user);
            System.out.println("Order Total: " + orderTotal);

            var order = ShopOrder.builder()
                    .userId(user)
                    .totalAmount(orderTotal)
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

}
