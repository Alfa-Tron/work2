package com.example.work4.repository;

import com.example.work4.model.City;
import com.example.work4.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface T3repository extends JpaRepository<Song, Long> {
    List<Song> findFirst23ByOrderByYear();
    @Query("SELECT SUM(c.year) as sum, MIN(c.year) as min, MAX(c.year) as max, AVG(c.year) as average FROM Song c")
    Map<String, Object> aggregateFloors();
    @Query("SELECT c.mode, COUNT(c.mode) as count FROM Song c GROUP BY c.mode")
    List<Map<String, Object>> countMode();
    List<Song> findFirst28ByYearGreaterThanOrderByTempo(int year);

}
