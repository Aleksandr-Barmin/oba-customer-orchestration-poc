package uk.co.santander.onboarding.services.orchestration.service;

import org.springframework.statemachine.StateMachineContext;

public interface StateMachineRepository<S, E, T> {
    boolean isAvailable(T applicationId);

    void write(StateMachineContext<S, E> context, T contextObj) throws Exception;

    StateMachineContext<S, E> read(T contextObj) throws Exception;
}
