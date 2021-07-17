package com.example.application.services;

import com.example.application.models.Terminal;

import java.util.Set;

public interface TerminalService {
    Set<Terminal> getList();
    Set<Terminal>getList(String filter,Long systemUserId);
    Terminal save(Terminal t);
    void delete(Terminal t);
}
