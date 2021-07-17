package com.example.application.repositories;

import com.example.application.models.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TerminalRepository extends CrudRepository<Terminal,Long> , JpaRepository<Terminal,Long> {
        List<Terminal> findByNameContainingAndSystemUserId(String name, Long systemUserId);
}
