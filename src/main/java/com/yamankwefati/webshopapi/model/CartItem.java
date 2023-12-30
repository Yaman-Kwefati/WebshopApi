package com.yamankwefati.webshopapi.model;

import lombok.Data;

@Data
public class CartItem {
    private String productName;
    private long quantity;
    private int price;
    private int productId;
}

