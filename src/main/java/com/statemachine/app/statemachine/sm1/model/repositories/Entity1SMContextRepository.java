package com.statemachine.app.statemachine.sm1.model.repositories;


import java.util.Optional;

import com.statemachine.app.statemachine.sm1.model.Entity1SMContext;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Entity1SMContextRepository extends JpaRepository<Entity1SMContext, Long> {
    Optional<Entity1SMContext> findByEntity1Id(Long id);
}
