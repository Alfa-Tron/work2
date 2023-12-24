package com.example.work4.repository;

import com.example.work4.model.Product;
import com.example.work4.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface T4repository extends JpaRepository<Product, Long> {
    void deleteByName(String name);
    Product findByName(String name);
    List<Product> findTop10ByOrderByCountDesc();
    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findDistinctCategories();
    List<Product> findByCategory(String ca);
}
