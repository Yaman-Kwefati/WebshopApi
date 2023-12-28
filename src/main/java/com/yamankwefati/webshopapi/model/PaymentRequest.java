package com.yamankwefati.webshopapi.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaymentRequest {
    private List<CartItem> items;
    private String userEmail;
}
