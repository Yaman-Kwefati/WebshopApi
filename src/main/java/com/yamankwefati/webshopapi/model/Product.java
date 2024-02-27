package com.yamankwefati.webshopapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.yamankwefati.webshopapi.service.Views;
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
@Table(name = "Product", schema = "public")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Public.class)
    private Long id;
    @JsonView(Views.Public.class)
    private String name;
    @JsonView(Views.Public.class)
    private String description;
    @JsonView(Views.Public.class)
    private Double price;
    @ElementCollection
    @JsonView(Views.Public.class)
    private List<String> images;
    @JsonView(Views.Internal.class)
    private int stockQuantity;
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "productList")
    @JsonView(Views.Public.class)
    private List<Category> categoryList;
}
