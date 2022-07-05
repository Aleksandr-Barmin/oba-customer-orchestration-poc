package uk.co.santander.onboarding.services.orchestration.state;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import uk.co.santander.onboarding.services.orchestration.service.StateMachineListener;
import uk.co.santander.onboarding.services.orchestration.state.action.OnGetApplicantData;
import uk.co.santander.onboarding.services.orchestration.state.action.OnGetApplicantDataFailure;
import uk.co.santander.onboarding.services.orchestration.state.action.OnMachineInitialization;
import uk.co.santander.onboarding.services.orchestration.state.guard.CheckApplicantRetrieved;

import java.util.EnumSet;

@Slf4j
@Configuration
@EnableStateMachineFactory
public class StateMachineDefinition extends EnumStateMachineConfigurerAdapter<OrchestrationState, OrchestrationEvent> {
    public static final String MACHINE_NAME = "orchestration_machine";

    @Autowired
    private StateMachineListener listener;

    @Autowired
    private OnMachineInitialization onMachineInitialization;

    @Autowired
    private OnGetApplicantData onGetApplicantData;

    @Autowired
    private OnGetApplicantDataFailure onGetApplicantDataFailure;

    @Autowired
    private CheckApplicantRetrieved checkApplicantRetrieved;

    @Override
    public void configure(StateMachineTransitionConfigurer<OrchestrationState, OrchestrationEvent> transitions) throws Exception {
        transitions
                .withExternal()
                    .source(OrchestrationState.MACHINE_CREATED)
                    .target(OrchestrationState.MACHINE_INITIALIZED)
                    .event(OrchestrationEvent.START_EXECUTION)
                    .action(onMachineInitialization)
                    .and()
                .withExternal()
                    .source(OrchestrationState.MACHINE_INITIALIZED)
                    .target(OrchestrationState.GET_APPLICANT_DATA_STATE)
                    .action(onGetApplicantData)
                    .and()
                .withJunction()
                    .source(OrchestrationState.GET_APPLICANT_DATA_STATE)
                    .first(
                            OrchestrationState.VALIDATE_APPLICANT_DATA_STATE,
                            checkApplicantRetrieved
                    )
                    .last(
                            OrchestrationState.GET_APPLICANT_DATA_FAILURE_STATE,
                            onGetApplicantDataFailure
                    )
                    .and();
    }

    @Override
    public void configure(StateMachineStateConfigurer<OrchestrationState, OrchestrationEvent> states) throws Exception {
        states.withStates()
                .initial(OrchestrationState.MACHINE_CREATED)
                .junction(OrchestrationState.GET_APPLICANT_DATA_STATE)
                .end(OrchestrationState.GET_APPLICANT_DATA_FAILURE_STATE)
                .states(EnumSet.allOf(OrchestrationState.class));
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<OrchestrationState, OrchestrationEvent> config) throws Exception {
        config.withConfiguration()
                .autoStartup(false)
                .listener(listener)
                .machineId(MACHINE_NAME);
    }
}
