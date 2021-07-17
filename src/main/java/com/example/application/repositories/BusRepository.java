package com.example.application.repositories;

import com.example.application.models.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BusRepository extends CrudRepository<Bus,Long>, JpaRepository<Bus,Long> {
    List<Bus> findByMarkaContainingAndSystemUserId(String marka, Long systemUserId);
}
