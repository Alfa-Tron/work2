package com.example.work4.repository;

import com.example.work4.model.BuildingInfo;
import com.example.work4.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface T2repository extends JpaRepository<BuildingInfo, Long> {
    BuildingInfo findByCityName(String name);
}
