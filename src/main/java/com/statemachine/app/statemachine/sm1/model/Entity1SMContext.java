package com.statemachine.app.statemachine.sm1.model;


import lombok.*;
import jakarta.persistence.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "entity1_sm_context")
public class Entity1SMContext {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long entity1Id;
    private String state;
    private String context;
}
