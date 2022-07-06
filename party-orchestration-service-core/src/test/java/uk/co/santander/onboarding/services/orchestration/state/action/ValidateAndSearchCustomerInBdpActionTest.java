package uk.co.santander.onboarding.services.orchestration.state.action;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.co.santander.onboarding.services.orchestration.model.ApplicantValidationStatus;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateConstants;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateContextHelper;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static reactor.core.publisher.Mono.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        StateContextHelper.class,
        ValidateAndSearchCustomerInBdpAction.class
})
class ValidateAndSearchCustomerInBdpActionTest {
    @Autowired
    private ValidateAndSearchCustomerInBdpAction uut;

    @Mock
    private StateContext context;

    @Mock
    private ExtendedState extendedState;

    @Test
    @DisplayName("Context should start")
    void check_contextStarts() {
        assertThat(uut).isNotNull();
    }

    @Test
    @DisplayName("Applicant ID should be in context")
    void execute_shouldHaveApplicantIdInContext() {
        when(context.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(Map.of());

        assertThatThrownBy(() -> {
            uut.execute(context);
        })
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Application ID should be provided");
    }

    @ParameterizedTest
    @EnumSource(
            value = ApplicantValidationStatus.class,
            mode = EnumSource.Mode.EXCLUDE,
            names = "SUCCESS"
    )
    @DisplayName("Validation status should be positive")
    void execute_validationStatusShouldBePositive(final ApplicantValidationStatus validationStatus) {
        when(context.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(Map.of(
                StateConstants.APPLICATION_ID, UUID.randomUUID(),
                StateConstants.APPLICANT_VALIDATION_STATUS, validationStatus
        ));

        assertThatThrownBy(() -> {
            uut.execute(context);
        })
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Validation result should be positive");
    }


}