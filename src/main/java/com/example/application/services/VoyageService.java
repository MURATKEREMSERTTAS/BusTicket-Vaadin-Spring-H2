package com.example.application.services;

import com.example.application.models.SystemUser;
import com.example.application.models.Voyage;

import java.util.Set;

public interface VoyageService {
    Set<Voyage> getList();
    Set<Voyage> getList(String filter, Long systemUserId);
    Voyage save(Voyage v);
    void delete(Voyage v);
}
