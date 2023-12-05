package com.yamankwefati.webshopapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Category", schema = "public")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    private String categoryName;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "ProductCategory",
    joinColumns = @JoinColumn(name = "productId"),
    inverseJoinColumns = @JoinColumn(name = "categoryId"))
    private List<Product> productList;
}
