package com.example.application.services;

import com.example.application.models.Bus;
import com.example.application.repositories.BusRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class BusServiceImp implements BusService {

    private final BusRepository busRepository;

    public BusServiceImp(BusRepository busRepository) {
        this.busRepository = busRepository;
    }


    @Override
    public Set<Bus> getList() {
        Set<Bus> busSet =new HashSet<>();
        busRepository.findAll().iterator().forEachRemaining(busSet::add);
        return busSet;
    }

    @Override
    public Set<Bus> getList(String filter, Long systemUserId) {
        Set<Bus> busSet =new HashSet<>();
        busRepository.findByMarkaContainingAndSystemUserId(filter,systemUserId).iterator().forEachRemaining(busSet::add);
        return busSet;
    }


    @Override
    public Bus save(Bus b) {
        return busRepository.save(b);
    }

    @Override
    public void delete(Bus b) { busRepository.delete(b); }
}

