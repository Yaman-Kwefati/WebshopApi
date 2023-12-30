package com.yamankwefati.webshopapi.model;

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
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status orderStatus = Status.PLACED;
    @Builder.Default
    private LocalDate orderDate = LocalDate.now();
    private Double totalAmount;
}
