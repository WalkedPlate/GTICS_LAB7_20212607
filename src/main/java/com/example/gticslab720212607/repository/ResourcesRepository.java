package com.example.gticslab720212607.repository;

import com.example.gticslab720212607.entity.Resources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourcesRepository extends JpaRepository<Resources, Integer> {
}
