package com.yamankwefati.webshopapi.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shop_order", schema = "public")
public class ShopOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status orderStatus = Status.PLACED;
    private LocalDate orderDate;
    private Double totalAmount;
}
