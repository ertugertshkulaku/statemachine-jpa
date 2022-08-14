package com.statemachine.app.statemachine.sm1.conf;


import com.statemachine.app.statemachine.sm1.model.*;
import com.statemachine.app.statemachine.sm1.model.repositories.Entity1SMContextRepository;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.util.UUID;
import java.util.Base64;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.springframework.stereotype.Component;
import org.springframework.messaging.MessageHeaders;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.kryo.UUIDSerializer;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.kryo.MessageHeadersSerializer;
import org.springframework.statemachine.kryo.StateMachineContextSerializer;

@Slf4j
@Component
public class Entity1StateMachinePersist implements StateMachinePersist<Entity1State, Entity1Event, String> {

    private final Entity1SMContextRepository repository;

    public Entity1StateMachinePersist(Entity1SMContextRepository repository) {
        this.repository = repository;
    }

    @Override
    public void write(StateMachineContext<Entity1State, Entity1Event> context, String contextObj) {
        log.info("start to write to database {} {} {}", context, serialize(context).length(), contextObj);

        Optional<Entity1SMContext> machineContext = repository.findByEntity1Id(Long.valueOf(contextObj));
        if (machineContext.isPresent()) {
            machineContext.ifPresent(ctx -> {
                ctx.setState(context.getState().name());
                ctx.setContext(serialize(context));
                repository.save(ctx);
            });
        } else {
            repository.save(Entity1SMContext
                                .builder()
                                .entity1Id(Long.valueOf(context.getId()))
                                .context(serialize(context))
                                .state(context.getState().name())
                                .build());
        }
    }

    @Override
    public StateMachineContext<Entity1State, Entity1Event> read(String contextObj) {
        log.info("start to read from database {}", contextObj);
        return repository
                .findByEntity1Id(Long.valueOf(contextObj))
                .map(context -> {
                    StateMachineContext<Entity1State, Entity1Event> stateMachineContext = deserialize(context.getContext());
                    log.info("read the current state {} ", stateMachineContext.getState().name());
                    return stateMachineContext;
                })
                .orElse(null);
    }

    private String serialize(StateMachineContext<Entity1State, Entity1Event> context) {
        return Optional.ofNullable(context).map(ctx -> {
            Kryo kryo = kryoThreadLocal.get();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Output output = new Output(outputStream);
            kryo.writeObject(output, context);
            byte[] bytes = output.toBytes();
            output.flush();
            output.close();
            return Base64.getEncoder().encodeToString(bytes);
        }).orElse(null);
    }

    @SuppressWarnings("unchecked")
    private StateMachineContext<Entity1State, Entity1Event> deserialize(String data) {
        return Optional.ofNullable(data).map(str -> {
            Kryo kryo = kryoThreadLocal.get();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
            Input input = new Input(inputStream);
            return kryo.readObject(input, StateMachineContext.class);
        }).orElse(null);
    }

    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.addDefaultSerializer(StateMachineContext.class, new StateMachineContextSerializer());
        kryo.addDefaultSerializer(MessageHeaders.class, new MessageHeadersSerializer());
        kryo.addDefaultSerializer(UUID.class, new UUIDSerializer());
        return kryo;
    });
}
