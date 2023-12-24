package com.example.work4.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "song", schema = "d")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String artist;
    private String song;
    private String song1;
    private String duration_ms;
    private Integer year;
    private String tempo;
    private String genre;
    private String mode;
    private String speechiness;
    private String acousticness;
    private String instrumentalness;
    private String explicit;
    private String loudness;


}