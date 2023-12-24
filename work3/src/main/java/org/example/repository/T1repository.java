package org.example.repository;


import org.example.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface T1repository extends JpaRepository<City, Long> {
}
