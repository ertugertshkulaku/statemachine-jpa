package com.statemachine.app.statemachine.sm2.conf;


import java.util.EnumSet;
import com.statemachine.app.statemachine.sm2.model.*;
import static com.statemachine.app.statemachine.sm2.model.Entity2Event.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;

@Configuration
public class Entity2StateMachineConfig {

    @Configuration
    public static class Entity2PersisterConfig {
        @Bean
        public StateMachineRuntimePersister<Entity2State, Entity2Event, String> stateMachineRuntimePersister(
                Entity2StateMachinePersist entity2StateMachinePersist) {
            return new Entity2PersistingStateMachineInterceptor(entity2StateMachinePersist);
        }
    }

    @Configuration
    @EnableStateMachineFactory(name = "entity2StateMachineFactory")
    public static class MachineConfig extends EnumStateMachineConfigurerAdapter<Entity2State, Entity2Event> {
        @Autowired
        private StateMachineRuntimePersister<Entity2State, Entity2Event, String> stateMachinePersister;

        @Override
        public void configure(StateMachineConfigurationConfigurer<Entity2State, Entity2Event> config) throws Exception {
            config.withPersistence().runtimePersister(stateMachinePersister);
            config.withConfiguration().autoStartup(false);
        }

        @Override
        public void configure(StateMachineStateConfigurer<Entity2State, Entity2Event> states) throws Exception {
            states.withStates()
                    .initial(Entity2State.FREE)
                    .states(EnumSet.allOf(Entity2State.class));
        }

        @Override
        public void configure(StateMachineTransitionConfigurer<Entity2State, Entity2Event> transitions) throws Exception {
            transitions
                .withExternal()
                    .source(Entity2State.FREE).target(Entity2State.PAID).event(PAY).and()
                .withExternal()
                    .source(Entity2State.PAID).target(Entity2State.DELIVERED).event(DELIVER);
        }
    }

    @Configuration
    public static class entity2ServiceConfig {
        @Bean
        public StateMachineService<Entity2State, Entity2Event> entity2StateMachineService(
                StateMachineFactory<Entity2State, Entity2Event> stateMachineFactory,
                StateMachineRuntimePersister<Entity2State, Entity2Event, String> stateMachineRuntimePersister) {
            return new DefaultStateMachineService<>(stateMachineFactory, stateMachineRuntimePersister);
        }
    }
}
