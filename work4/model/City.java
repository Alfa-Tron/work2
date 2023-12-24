package com.example.work4.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;


@Entity
@Table(name = "city", schema = "d")
@JsonIgnoreProperties(ignoreUnknown = true)
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("street")
    private String street;

    @JsonProperty("city")
    private String city;

    @JsonProperty("zipcode")
    private int zipcode;

    @JsonProperty("floors")
    private int floors;

    @JsonProperty("year")
    private int year;

    @JsonProperty("parking")
    private boolean parking;

    @JsonProperty("prob_price")
    private long prob_price;

    @JsonProperty("views")
    private int views;
    public City(String name) {
        this.name = name;
    }

    public City() {

    }

    public String getName() {
        return name;
    }
}