package com.statemachine.app.model1;


import lombok.*;
import jakarta.persistence.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "entity1")
@Builder
public class Entity1 {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
}
