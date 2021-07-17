package com.example.application.services;

import com.example.application.models.Voyage;
import com.example.application.repositories.VoyageRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class VoyageServiceImp implements VoyageService{

    private final VoyageRepository voyageRepository;

    public VoyageServiceImp(VoyageRepository voyageRepository) {
        this.voyageRepository = voyageRepository;
    }

    @Override
    public Set<Voyage> getList() {
        Set<Voyage> voyageSet =new HashSet<>();
        voyageRepository.findAll().iterator().forEachRemaining(voyageSet::add);
        return voyageSet;
    }
    @Override
    public Set<Voyage> getList(String filter,Long systemUserId) {
        Set<Voyage> voyages =new HashSet<>();
        voyageRepository.findByKalkisGunuContainingAndSystemUserId(filter,systemUserId).iterator().forEachRemaining(voyages::add);
        return voyages;
    }


    @Override
    public Voyage save(Voyage v) {
        return voyageRepository.save(v);
    }

    @Override
    public void delete(Voyage v) {
        voyageRepository.delete(v);
    }
}
