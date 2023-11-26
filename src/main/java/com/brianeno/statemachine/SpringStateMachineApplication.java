package com.brianeno.statemachine;

import com.brianeno.statemachine.config.VehicleStateMachineConfig;
import com.brianeno.statemachine.domain.VehicleEvents;
import com.brianeno.statemachine.domain.VehicleStates;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

@SpringBootApplication
@RequiredArgsConstructor
public class SpringStateMachineApplication implements CommandLineRunner {


    private final StateMachineFactory<VehicleStates, VehicleEvents> stateMachineFactory;

    public static void main(String[] args) {
        SpringApplication.run(SpringStateMachineApplication.class, args);
    }

    @Override
    public void run(String... args) {

        StateMachine<VehicleStates, VehicleEvents> stateMachine = stateMachineFactory.getStateMachine(VehicleStateMachineConfig.MACHINE_ID);

        // since version 3.x need to send events reactively
        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(VehicleEvents.SPECIFY)
            .setHeader(VehicleStateMachineConfig.VEHICLE_ID_HEADER, 1)
            .build())).subscribe();

        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(VehicleEvents.START_ASSEMBLY)
            .setHeader(VehicleStateMachineConfig.VEHICLE_ID_HEADER, 1)
            .build())).subscribe();

        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(VehicleEvents.INTERIOR_COMPLETE)
            .setHeader(VehicleStateMachineConfig.VEHICLE_ID_HEADER, 1)
            .build())).subscribe();

        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(VehicleEvents.EXTERIOR_COMPLETE)
            .setHeader(VehicleStateMachineConfig.VEHICLE_ID_HEADER, 1)
            .build())).subscribe();

        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(VehicleEvents.ASSEMBLY_COMPLETE)
            .setHeader(VehicleStateMachineConfig.VEHICLE_ID_HEADER, 1)
            .build())).subscribe();

        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(VehicleEvents.SHIP)
            .setHeader(VehicleStateMachineConfig.VEHICLE_ID_HEADER, 1)
            .build())).subscribe();
    }
}