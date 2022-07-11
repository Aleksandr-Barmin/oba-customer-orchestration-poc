package uk.co.santander.onboarding.services.orchestration.state;

/**
 * Events that can be sent to state machine.
 */
public enum OrchestrationEvent {
    /**
     * Initial event which is used to kickstart the SM.
     */
    AUTHORIZE_EVENT,

    /**
     * Second step of the execution.
     */
    EXECUTE_EVENT
}
