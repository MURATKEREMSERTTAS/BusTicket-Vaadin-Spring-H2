package com.example.application.repositories;

import com.example.application.models.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoyageRepository extends CrudRepository<Voyage,Long> , JpaRepository<Voyage,Long> {
    List<Voyage> findByKalkisGunuContainingAndSystemUserId(String kalkisGunu,Long systemUserId);
}
