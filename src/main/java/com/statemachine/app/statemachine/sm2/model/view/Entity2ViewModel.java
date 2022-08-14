package com.statemachine.app.statemachine.sm2.model.view;

import com.statemachine.app.model2.Entity2;
import com.statemachine.app.statemachine.sm2.model.Entity2SMContext;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Entity2ViewModel {
    private Entity2 entity2;
    private Entity2SMContext context;
}
