package com.statemachine.app.statemachine.sm1.model.view;


import com.statemachine.app.statemachine.sm1.model.Entity1SMContext;
import lombok.*;
import com.statemachine.app.model1.Entity1;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Entity1ViewModel {
    private Entity1 entity1;
    private Entity1SMContext context;
}
