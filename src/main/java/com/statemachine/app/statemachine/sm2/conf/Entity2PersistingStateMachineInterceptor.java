package com.statemachine.app.statemachine.sm2.conf;


import com.statemachine.app.statemachine.sm2.model.*;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.persist.AbstractPersistingStateMachineInterceptor;

public class Entity2PersistingStateMachineInterceptor
        extends AbstractPersistingStateMachineInterceptor<Entity2State, Entity2Event, String>
        implements StateMachineRuntimePersister<Entity2State, Entity2Event, String> {

    private final Entity2StateMachinePersist persist;

    public Entity2PersistingStateMachineInterceptor(Entity2StateMachinePersist entity2StateMachinePersist) {
        this.persist = entity2StateMachinePersist;
    }

    @Override
    public void write(StateMachineContext<Entity2State, Entity2Event> context, String contextObj) {
        persist.write(context, contextObj);
    }

    @Override
    public StateMachineContext<Entity2State, Entity2Event> read(String contextObj) {
        return persist.read(contextObj);
    }

    @Override
    public StateMachineInterceptor<Entity2State, Entity2Event> getInterceptor() {
        return this;
    }
}
