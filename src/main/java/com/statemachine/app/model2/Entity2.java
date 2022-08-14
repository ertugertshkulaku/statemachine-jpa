package com.statemachine.app.model2;


import lombok.*;
import lombok.Data;
import jakarta.persistence.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "entity2")
@Builder
public class Entity2 {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
}
