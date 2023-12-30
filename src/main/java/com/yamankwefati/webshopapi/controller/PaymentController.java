package com.yamankwefati.webshopapi.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.yamankwefati.webshopapi.model.ApiResponse;
import com.yamankwefati.webshopapi.model.CartItem;
import com.yamankwefati.webshopapi.model.PaymentRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/v1/payment")
public class PaymentController {

@PostMapping("/create-checkout-session")
public ApiResponse<String> createCheckoutSession(@RequestBody PaymentRequest paymentRequest) {
    try {
        SessionCreateParams.Builder sessionBuilder = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.IDEAL)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://159.223.236.93/success")
                .setCancelUrl("http://159.223.236.93/cancel");

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
        return new ApiResponse<>(HttpStatus.OK, session.getUrl());

    } catch (StripeException e) {
        return new ApiResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
}
