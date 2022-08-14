package com.statemachine.app.statemachine.sm1.conf;


import com.statemachine.app.statemachine.sm1.model.*;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.persist.AbstractPersistingStateMachineInterceptor;

public class Entity1PersistingStateMachineInterceptor
        extends AbstractPersistingStateMachineInterceptor<Entity1State, Entity1Event, String>
        implements StateMachineRuntimePersister<Entity1State, Entity1Event, String> {

    private final Entity1StateMachinePersist persist;

    public Entity1PersistingStateMachineInterceptor(Entity1StateMachinePersist entity1StateMachinePersist) {
        this.persist = entity1StateMachinePersist;
    }

    @Override
    public void write(StateMachineContext<Entity1State, Entity1Event> context, String contextObj) {
        persist.write(context, contextObj);
    }

    @Override
    public StateMachineContext<Entity1State, Entity1Event> read(String contextObj) {
        return persist.read(contextObj);
    }

    @Override
    public StateMachineInterceptor<Entity1State, Entity1Event> getInterceptor() {
        return this;
    }
}
