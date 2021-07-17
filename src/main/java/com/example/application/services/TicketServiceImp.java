package com.example.application.services;

import com.example.application.models.Ticket;
import com.example.application.repositories.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
@Service
public class TicketServiceImp implements TicketService{

    private TicketRepository ticketRepository;

    public TicketServiceImp(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Set<Ticket> getList() {
        Set<Ticket> tickets =new HashSet<>();
        ticketRepository.findAll().iterator().forEachRemaining(tickets::add);
        return tickets;
    }

    @Override
    public Set<Ticket> getList(String filter, Long systemUserId) {
        Set<Ticket> tickets =new HashSet<>();
        ticketRepository.findByAdContainingAndSystemUserId(filter,systemUserId).iterator().forEachRemaining(tickets::add);
        return tickets;
    }

    @Override
    public Ticket save(Ticket t) {
        return ticketRepository.save(t);
    }

    @Override
    public void delete(Ticket t) {
        ticketRepository.delete(t);
    }
}
