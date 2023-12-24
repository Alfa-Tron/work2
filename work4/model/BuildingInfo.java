package com.example.work4.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "building_info", schema = "d")
public class BuildingInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "name")
    @JsonProperty("name")
    private City city;

    @JsonProperty("rating")
    private Double rating;

    @JsonProperty("convenience")
    private Integer convenience;

    @JsonProperty("security")
    private Integer security;

    @JsonProperty("functionality")
    private Integer functionality;

    @JsonProperty("comment")
    private String comment;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}