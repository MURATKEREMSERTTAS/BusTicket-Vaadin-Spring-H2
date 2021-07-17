package com.example.application.services;

import com.example.application.models.Bus;

import java.util.Set;

public interface BusService {
    Set<Bus> getList();
    Set<Bus> getList(String filter,Long systemUserId);
    Bus save (Bus b);
    void delete(Bus b);

}
