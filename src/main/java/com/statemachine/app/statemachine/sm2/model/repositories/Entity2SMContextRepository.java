package com.statemachine.app.statemachine.sm2.model.repositories;


import java.util.Optional;

import com.statemachine.app.statemachine.sm2.model.Entity2SMContext;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Entity2SMContextRepository extends JpaRepository<Entity2SMContext, Long> {
    Optional<Entity2SMContext> findByEntity2Id(Long id);
}
