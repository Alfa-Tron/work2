package com.example.work4.repository;

import com.example.work4.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface T1repository extends JpaRepository<City, Long> {

    City findByName(String name);
    List<City> findFirst23ByOrderByFloors();

    @Query("SELECT SUM(c.floors) as sum, MIN(c.floors) as min, MAX(c.floors) as max, AVG(c.floors) as average FROM City c")
    Map<String, Object> aggregateFloors();

    @Query("SELECT c.parking, COUNT(c.parking) as count FROM City c GROUP BY c.parking")
    List<Map<String, Object>> countParking();

    List<City> findFirst23ByYearGreaterThanOrderByFloors(int year);
}