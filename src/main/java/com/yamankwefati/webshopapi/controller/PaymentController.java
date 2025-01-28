package com.yamankwefati.webshopapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.StripeException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.yamankwefati.webshopapi.dao.payment.PaymentDAO;
import com.yamankwefati.webshopapi.dao.product.ProductDAO;
import com.yamankwefati.webshopapi.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value = "/api/v1/payment")
public class PaymentController {
    private final PaymentDAO paymentDAO;
    private final ProductDAO productDAO;

    public PaymentController( PaymentDAO paymentDAO,
                              ProductDAO productDAO) {
        this.productDAO = productDAO;
        this.paymentDAO = paymentDAO;
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
                    .setSuccessUrl("https://cholietalie.nl/success")
                    .setAllowPromotionCodes(true)
                    .setCancelUrl("https://cholietalie.nl/cancel");

            for (CartItem item : paymentRequest.getItems()) {
                Optional<Product> optionalProduct = this.productDAO.getProductById((long) item.getProductId());
                if (optionalProduct.isEmpty()) return new ApiResponse<>(HttpStatus.NOT_FOUND, "Can't Find Product!");
                Product product = optionalProduct.get();
                sessionBuilder.addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(item.getQuantity())
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("eur")
                                                .setUnitAmount(BigDecimal.valueOf(product.getPrice())
                                                        .multiply(BigDecimal.valueOf(100))
                                                        .longValue())
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(product.getName())
                                                                .build()
                                                )
                                                .build())
                                .build()
                );
            }

            Session session = Session.create(sessionBuilder.build());
            return new ApiResponse<>(HttpStatus.OK, session.getUrl());

        } catch (StripeException e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/webhook")
    public ApiResponse<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
       // String webhookSecret = ; //Live
//        String webhookSecret = ; //test
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(payload);

            JsonNode sessionNode = rootNode.path("data").path("object");
            String customerEmail = sessionNode.path("customer_details").path("email").asText();
            String cartItemsJson = sessionNode.path("metadata").path("cartItems").asText();
            Long amountSubtotal = sessionNode.path("amount_subtotal").asLong();
            Long totalAmount = sessionNode.path("amount_total").asLong();
            List<CartItem> cartItems = objectMapper.readValue(cartItemsJson, new TypeReference<List<CartItem>>() {});

            if (!cartItems.isEmpty() && !customerEmail.isEmpty()) {
//                long amountTotal = this.paymentDAO.calculateOrderTotalAmount(cartItems);
                this.paymentDAO.processSuccessfulPayment(totalAmount, customerEmail, cartItems, amountSubtotal);
            }

            return new ApiResponse<>(HttpStatus.OK, "OK");
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, "Webhook Error: " + e.getMessage());
        }
    }
}
