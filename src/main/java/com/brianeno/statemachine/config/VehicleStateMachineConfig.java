package com.brianeno.statemachine.config;

import com.brianeno.statemachine.domain.VehicleEvents;
import com.brianeno.statemachine.domain.VehicleStates;
import com.brianeno.statemachine.service.VehicleStateListener;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachineFactory
@AllArgsConstructor
public class VehicleStateMachineConfig extends EnumStateMachineConfigurerAdapter<VehicleStates, VehicleEvents> {

    public static final String VEHICLE_ID_HEADER = "vehicle_id";
    public static final String MACHINE_ID = "forkjoinMachineId";

    @Override
    public void configure(StateMachineStateConfigurer<VehicleStates, VehicleEvents> states) throws Exception {
        states.withStates()
            .region("R_MAIN")
            .initial(VehicleStates.VEHICLE_SOLD, ctx -> {
                System.out.println("Action sold");
            })
            .state(VehicleStates.VEHICLE_SPECIFIED, ctx -> {
                System.out.println("Action specified");
            })
            .state(VehicleStates.VEHICLE_PACKAGED, ctx -> {
                System.out.println("Action package");
            })
            .state(VehicleStates.VEHICLE_SHIPPED, ctx -> {
                System.out.println("Action shipped");
            })
            .fork(VehicleStates.FORK_BEGIN_ASSEMBLY)
            .join(VehicleStates.JOIN_END_ASSEMBLY)
            .end(VehicleStates.VEHICLE_SHIPPED)
            .and()
            .withStates()
            .parent(VehicleStates.FORK_BEGIN_ASSEMBLY)
            .region("R_EXTERIOR")
            .initial(VehicleStates.VEHICLE_EXTERIOR_ASSEMBLING)
            .end(VehicleStates.VEHICLE_EXTERIOR_ASSEMBLED)
            .and()
            .withStates()
            .parent(VehicleStates.FORK_BEGIN_ASSEMBLY)
            .region("R_INTERIOR")
            .initial(VehicleStates.VEHICLE_INTERIOR_ASSEMBLING)
            .end(VehicleStates.VEHICLE_INTERIOR_ASSEMBLED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<VehicleStates, VehicleEvents> transitions) throws Exception {
        transitions
            .withExternal()
            .source(VehicleStates.VEHICLE_SOLD)
            .target(VehicleStates.VEHICLE_SPECIFIED)
            .event(VehicleEvents.SPECIFY)
            .and()
            .withExternal()
            .source(VehicleStates.VEHICLE_SPECIFIED)
            .target(VehicleStates.FORK_BEGIN_ASSEMBLY)
            .event(VehicleEvents.START_ASSEMBLY)
            .and()
            .withFork()
            .source(VehicleStates.FORK_BEGIN_ASSEMBLY)
            .target(VehicleStates.VEHICLE_INTERIOR_ASSEMBLING)
            .target(VehicleStates.VEHICLE_EXTERIOR_ASSEMBLING)
            .and()
            .withJoin()
            .source(VehicleStates.VEHICLE_INTERIOR_ASSEMBLED)
            .source(VehicleStates.VEHICLE_EXTERIOR_ASSEMBLED)
            .target(VehicleStates.JOIN_END_ASSEMBLY)
            .and()
            .withExternal()
            .source(VehicleStates.VEHICLE_INTERIOR_ASSEMBLING)
            .target(VehicleStates.VEHICLE_INTERIOR_ASSEMBLED)
            .event(VehicleEvents.INTERIOR_COMPLETE)
            .and()
            .withExternal()
            .source(VehicleStates.VEHICLE_EXTERIOR_ASSEMBLING)
            .target(VehicleStates.VEHICLE_EXTERIOR_ASSEMBLED)
            .event(VehicleEvents.EXTERIOR_COMPLETE)
            .and()
            .withExternal()
            .source(VehicleStates.JOIN_END_ASSEMBLY)
            .target(VehicleStates.VEHICLE_PACKAGED)
            .event(VehicleEvents.ASSEMBLY_COMPLETE)
            .and()
            .withExternal()
            .source(VehicleStates.VEHICLE_PACKAGED)
            .target(VehicleStates.VEHICLE_SHIPPED)
            .event(VehicleEvents.SHIP);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<VehicleStates, VehicleEvents> config) throws Exception {
        config.withConfiguration()
            .machineId(MACHINE_ID)
            .autoStartup(true)
            .listener(new VehicleStateListener());
    }
}
