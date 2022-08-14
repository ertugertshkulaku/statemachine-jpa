package com.statemachine.app.statemachine.sm2.controller;


import com.statemachine.app.model2.Entity2;
import com.statemachine.app.statemachine.sm2.model.Entity2State;
import org.springframework.statemachine.StateMachine;
import com.statemachine.app.statemachine.sm2.model.Entity2Event;
import org.springframework.statemachine.service.StateMachineService;
import com.statemachine.app.model2.repositories.Entity2Repository;
import com.statemachine.app.statemachine.sm2.model.view.Entity2ViewModel;
import com.statemachine.app.statemachine.sm2.model.repositories.Entity2SMContextRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.ResponseEntity.*;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@RestController
public class Entity2Controller {

    @Autowired
    private Entity2Repository entity2Repository;

    @Autowired
    private Entity2SMContextRepository entity2SMContextRepository;

    @Autowired
    private StateMachineService<Entity2State, Entity2Event> stateEntity2EventStateMachineService;

    @PostMapping(value = "/entity2")
    public ResponseEntity<Entity2> create(@RequestBody Entity2 entity2) {
        return ok(entity2Repository.save(entity2));
    }

    @GetMapping(value = "/entity2/{entity2Id}")
    public ResponseEntity<Entity2ViewModel> entity2ById(@PathVariable Long entity2Id) {
        return entity2Repository
                .findById(entity2Id)
                .map(entity2 -> Entity2ViewModel.builder().entity2(entity2))
                .flatMap(builder ->
                        entity2SMContextRepository
                                .findById(entity2Id)
                                .map(builder::context)
                                .map(Entity2ViewModel.Entity2ViewModelBuilder::build))
                .map(ResponseEntity::ok)
                .orElse(notFound().build());
    }

    @GetMapping(value = "/entity2/{entity2Id}/{event}")
    public ResponseEntity<Entity2> changeEvent(@PathVariable Long entity2Id, @PathVariable String event) {
        String machineId = String.valueOf(entity2Id);
        StateMachine<Entity2State, Entity2Event> sm = stateEntity2EventStateMachineService.acquireStateMachine(machineId);
        log.info("entity2Id {}, event {}", entity2Id, event);
        log.info("event {}", sm.getState().getId().name());

//        Message<Entity2Event> message = MessageBuilder.withPayload(Entity2Event.valueOf(event)).build();
//        sm.sendEvent(Mono.just(message))
//                .doOnComplete(() -> {
//                });

        if (sm.sendEvent(Entity2Event.valueOf(event))) {
            log.info("event sent {}", Entity2Event.valueOf(event));
            return entity2Repository.findById(entity2Id).map(ResponseEntity::ok).orElse(badRequest().build());
        }
        return badRequest().build();
    }

    @GetMapping(value = "/entity2/statemachines/{machineId}/acquire")
    public ResponseEntity<String> acquireStateMachine(@PathVariable String machineId) {
        stateEntity2EventStateMachineService.acquireStateMachine(machineId);
        return ok("success");
    }

    @GetMapping(value = "/entity2/statemachines/{machineId}/release")
    public ResponseEntity<String> releaseStateMachine(@PathVariable String machineId) {
        stateEntity2EventStateMachineService.releaseStateMachine(machineId);
        return ok("success");
    }
}
