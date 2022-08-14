package com.statemachine.app.statemachine.sm1.controller;


import com.statemachine.app.model1.Entity1;
import com.statemachine.app.statemachine.sm1.model.Entity1Event;
import com.statemachine.app.statemachine.sm1.model.Entity1State;
import com.statemachine.app.model1.repositories.Entity1Repository;
import org.springframework.statemachine.service.StateMachineService;
import com.statemachine.app.statemachine.sm1.model.view.Entity1ViewModel;
import com.statemachine.app.statemachine.sm1.model.repositories.Entity1SMContextRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.statemachine.StateMachine;
import static org.springframework.http.ResponseEntity.*;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@RestController
public class Entity1Controller {

    @Autowired
    private Entity1Repository entity1Repository;

    @Autowired
    private Entity1SMContextRepository entity1SMContextRepository;

    @Autowired
    private StateMachineService<Entity1State, Entity1Event> stateEntity1EventStateMachineService;

    @PostMapping(value = "/entity1")
    public ResponseEntity<Entity1> create(@RequestBody Entity1 entity2) {
        return ok(entity1Repository.save(entity2));
    }

    @GetMapping(value = "/entity1/{entity1Id}")
    public ResponseEntity<Entity1ViewModel> entity2ById(@PathVariable Long entity1Id) {
        return entity1Repository
                .findById(entity1Id)
                .map(entity2 -> Entity1ViewModel.builder().entity1(entity2))
                .flatMap(builder ->
                        entity1SMContextRepository
                                .findById(entity1Id)
                                .map(builder::context)
                                .map(Entity1ViewModel.Entity1ViewModelBuilder::build))
                .map(ResponseEntity::ok)
                .orElse(notFound().build());
    }

    @GetMapping(value = "/entity1/{entity1Id}/{event}")
    public ResponseEntity<?> changeEvent(@PathVariable Long entity1Id, @PathVariable String event) {
        String machineId = String.valueOf(entity1Id);
        StateMachine<Entity1State, Entity1Event> sm = stateEntity1EventStateMachineService.acquireStateMachine(machineId);
        log.info("entity1Id {}, event {}", entity1Id, event);
        log.info("event {}", sm.getState().getId().name());

//        Message<Entity1Event> message = MessageBuilder.withPayload(Entity1Event.valueOf(event)).build();
//        sm.sendEvent(Mono.just(message))
//                .doOnComplete(() -> {
//                });

        if (sm.sendEvent(Entity1Event.valueOf(event))) {
            log.info("event sent {}", Entity1Event.valueOf(event));
            return entity1Repository.findById(entity1Id).map(ResponseEntity::ok).orElse(badRequest().build());
        }
        return badRequest().build();
    }

    @GetMapping(value = "/entity1/statemachines/{machineId}/acquire")
    public ResponseEntity<String> acquireStateMachine(@PathVariable String machineId) {
        stateEntity1EventStateMachineService.acquireStateMachine(machineId);
        return ok("success");
    }

    @GetMapping(value = "/entity1/statemachines/{machineId}/release")
    public ResponseEntity<String> releaseStateMachine(@PathVariable String machineId) {
        stateEntity1EventStateMachineService.releaseStateMachine(machineId);
        return ok("success");
    }
}
