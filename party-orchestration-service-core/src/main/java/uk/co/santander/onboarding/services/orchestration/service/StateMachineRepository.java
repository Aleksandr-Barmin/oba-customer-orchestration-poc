package uk.co.santander.onboarding.services.orchestration.service;

import org.springframework.statemachine.StateMachineContext;

/**
 * Higher-level abstraction on top of Spring State Machine persist to not only persist and restore
 * but also get information about all the state machines which are available currently.
 *
 * @param <S> type of states.
 * @param <E> type of events.
 * @param <T> type of context object - state machine ID.
 */
public interface StateMachineRepository<S, E, T> {
    boolean isAvailable(T applicationId);

    void write(StateMachineContext<S, E> context, T contextObj) throws Exception;

    StateMachineContext<S, E> read(T contextObj) throws Exception;
}
