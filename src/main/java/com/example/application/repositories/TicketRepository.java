package com.example.application.repositories;

import com.example.application.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TicketRepository extends CrudRepository<Ticket,Long> , JpaRepository<Ticket,Long> {
    List<Ticket> findByAdContainingAndSystemUserId(String ad,Long systemUserId);
}
