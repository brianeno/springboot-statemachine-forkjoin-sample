package com.brianeno.statemachine.service;

import com.brianeno.statemachine.domain.VehicleEvents;
import com.brianeno.statemachine.domain.VehicleStates;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.Optional;


public class VehicleStateListener extends StateMachineListenerAdapter<VehicleStates, VehicleEvents> {
    @Override
    public void stateChanged(State<VehicleStates, VehicleEvents> from, State<VehicleStates, VehicleEvents> to) {
        System.out.println("state changed from " + offNullableState(from) + " to " + offNullableState(to));
    }

    private Object offNullableState(State<VehicleStates, VehicleEvents> s) {
        return Optional.ofNullable(s).map(State::getId).orElse(null);
    }
}
