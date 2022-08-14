package com.statemachine.app.statemachine.sm2.model;

import lombok.*;
import jakarta.persistence.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "entity2_sm_context")
public class Entity2SMContext {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long entity2Id;
    private String state;
    private String context;
}
