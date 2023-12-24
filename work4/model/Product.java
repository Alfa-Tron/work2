package com.example.work4.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.DefaultValue;

@Entity
@Table(name = "product", schema = "d")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("category")
    private String category;

    @JsonProperty("fromCity")
    private String fromCity;

    @JsonProperty("isAvailable")
    private Boolean isAvailable;

    @JsonProperty("views")
    private Integer views;

    @Column(name = "count")
    private Integer count=0;
}
