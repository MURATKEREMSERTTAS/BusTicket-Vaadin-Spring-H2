package com.example.application.services;

import com.example.application.models.Terminal;
import com.example.application.repositories.TerminalRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TerminalServiceImp implements  TerminalService{

    private final TerminalRepository terminalRepository;

    public TerminalServiceImp(TerminalRepository terminalRepository) {
        this.terminalRepository = terminalRepository;
    }


    @Override
    public Set<Terminal> getList() {
        Set<Terminal> terminalSet =new HashSet<>();
        terminalRepository.findAll().iterator().forEachRemaining(terminalSet::add);
        return terminalSet;
    }

    @Override
    public Set<Terminal> getList(String filter, Long systemUserId) {
        Set<Terminal> terminals =new HashSet<>();
        terminalRepository.findByNameContainingAndSystemUserId(filter,systemUserId).iterator().forEachRemaining(terminals::add);
        return terminals;
    }

    @Override
    public Terminal save(Terminal t) {
        return terminalRepository.save(t);
    }

    @Override
    public void delete(Terminal t) {
        terminalRepository.delete(t);
    }
}
