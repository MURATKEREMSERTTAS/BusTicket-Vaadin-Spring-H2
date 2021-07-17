package com.example.application.services;

import com.example.application.models.Ticket;

import java.util.Set;

public interface TicketService {
    Set<Ticket> getList();
    Set<Ticket> getList(String filter,Long systemUserId);
    Ticket save(Ticket t);
    void delete(Ticket t);
}
