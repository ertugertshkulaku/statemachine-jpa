package com.statemachine.app.statemachine.sm1.conf;


import java.util.EnumSet;
import java.util.Optional;

import com.statemachine.app.statemachine.sm1.model.*;
import com.statemachine.app.statemachine.sm2.model.Entity2State;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;

@Slf4j
@Configuration
public class Entity1StateMachineConfig {

    @Configuration
    public static class Entity1PersisterConfig {
        @Bean
        public StateMachineRuntimePersister<Entity1State, Entity1Event, String> Entity1StateMachineRuntimePersister(
                Entity1StateMachinePersist entity1StateMachinePersist) {
            return new Entity1PersistingStateMachineInterceptor(entity1StateMachinePersist);
        }
    }

    @Configuration
    @EnableStateMachineFactory(name = "entity1StateMachineFactory")
    public static class MachineConfig extends EnumStateMachineConfigurerAdapter<Entity1State, Entity1Event> {
        @Autowired
        private StateMachineRuntimePersister<Entity1State, Entity1Event, String> stateMachineRuntimePersister;

        @Override
        public void configure(StateMachineConfigurationConfigurer<Entity1State, Entity1Event> config) throws Exception {
            config.withPersistence().runtimePersister(stateMachineRuntimePersister);
            config.withConfiguration().autoStartup(false);
        }

        @Override
        public void configure(StateMachineStateConfigurer<Entity1State, Entity1Event> states) throws Exception {
            states.withStates()
                    .initial(Entity1State.BACKLOG, developersWakeUpAction())
                    .state(Entity1State.IN_PROGRESS, weNeedCoffeeAction())
                    .state(Entity1State.TESTING, qaWakeUpAction())
                    .state(Entity1State.DONE, goToSleepAction())
                    .end(Entity1State.DONE)
                    .states(EnumSet.allOf(Entity1State.class));
        }

        private Action<Entity1State, Entity1Event> developersWakeUpAction() {
            return stateContext -> log.warn("Wake up lazy!");
        }

        private Action<Entity1State, Entity1Event> weNeedCoffeeAction() {
            return stateContext -> log.warn("No coffee!");
        }

        private Action<Entity1State, Entity1Event> qaWakeUpAction() {
            return stateContext -> log.warn("Wake up the testing team, the sun is high!");
        }

        private Action<Entity1State, Entity1Event> goToSleepAction() {
            return stateContext -> log.warn("All sleep! the client is satisfied.");
        }


        @Override
        public void configure(StateMachineTransitionConfigurer<Entity1State, Entity1Event> transitions) throws Exception {
            transitions
                .withExternal()
                    .source(Entity1State.BACKLOG)
                    .target(Entity1State.IN_PROGRESS)
                    .event(Entity1Event.START_FEATURE)
                    .and()
                    // DEVELOPERS:
                    .withExternal()
                    .source(Entity1State.IN_PROGRESS)
                    .target(Entity1State.TESTING)
                    .event(Entity1Event.FINISH_FEATURE)
                    .guard(alreadyDeployedGuard())
                    .and()
                    // QA-TEAM:
                    .withExternal()
                    .source(Entity1State.TESTING)
                    .target(Entity1State.DONE)
                    .event(Entity1Event.QA_CHECKED_UC)
                    .and()
                    .withExternal()
                    .source(Entity1State.TESTING)
                    .target(Entity1State.IN_PROGRESS)
                    .event(Entity1Event.QA_REJECTED_UC)
                    .and()
                    // ROCK-STAR:
                    .withExternal()
                    .source(Entity1State.BACKLOG)
                    .target(Entity1State.TESTING)
                    .event(Entity1Event.ROCK_STAR_DOUBLE_TASK)
                    .and()
                    // DEVOPS:
                    .withInternal()
                    .source(Entity1State.IN_PROGRESS)
                    .event(Entity1Event.DEPLOY)
                    .action(deployPreProd())
                    .and()
                    .withInternal()
                    .source(Entity1State.BACKLOG)
                    .event(Entity1Event.DEPLOY)
                    .action(deployPreProd());
        }
    }

    private static Guard<Entity1State, Entity1Event> alreadyDeployedGuard() {
        return context -> Optional.ofNullable(context.getExtendedState().getVariables().get("deployed"))
                .map(v -> (boolean) v)
                .orElse(false);
    }

    private static Action<Entity1State, Entity1Event> deployPreProd() {
        return stateContext -> {
            log.warn("DEPLOY: Rolling out to pre-production.");
            stateContext.getExtendedState().getVariables().put("deployed", true);
        };
    }

    @Configuration
    public static class Entity1ServiceConfig {
        @Bean
        public StateMachineService<Entity1State, Entity1Event> entity1StateMachineService(
                StateMachineFactory<Entity1State, Entity1Event> stateMachineFactory,
                StateMachineRuntimePersister<Entity1State, Entity1Event, String> stateMachineRuntimePersister) {
            return new DefaultStateMachineService<>(stateMachineFactory, stateMachineRuntimePersister);
        }
    }
}
