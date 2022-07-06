package uk.co.santander.onboarding.services.orchestration.state.action;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.co.santander.onboarding.services.orchestration.model.ApplicantValidationStatus;
import uk.co.santander.onboarding.services.orchestration.service.ApplicationService;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateConstants;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateContextHelper;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        StateContextHelper.class,
        ApplicantDataValidationFailedAction.class
})
class ApplicantDataValidationFailedActionTest {
    @Autowired
    private ApplicantDataValidationFailedAction uut;

    @MockBean
    private ApplicationService applicationService;

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
    @DisplayName("Should throw exception when status info not available")
    void execute_shouldThrowExceptionWhenInfoIsNotAvailable() {
        when(context.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(Map.of());

        assertThatThrownBy(() -> {
            uut.execute(context);
        })
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Validation result should be in the context");
    }

    @Test
    @DisplayName("Validation result should be negative")
    void execute_shouldUseOnlyNegativeResults() {
        when(context.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(Map.of(
                StateConstants.APPLICANT_VALIDATION_STATUS, ApplicantValidationStatus.SUCCESS
        ));

        assertThatThrownBy(() -> {
            uut.execute(context);
        })
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Validation result should be negative");
    }

    @ParameterizedTest
    @CsvSource({
            "reason1",
            "reason2"
    })
    @DisplayName("Should save validation message to the applicant data")
    void execute_shouldSaveReasonToOnboardingState(final String reason) {
        final UUID applicantId = UUID.randomUUID();

        when(context.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(Map.of(
                StateConstants.APPLICATION_ID, applicantId,
                StateConstants.APPLICANT_VALIDATION_STATUS, ApplicantValidationStatus.NOT_SUCCESS,
                StateConstants.APPLICANT_VALIDATION_MESSAGE, reason
        ));

        uut.execute(context);

        verify(applicationService, times(1)).createRecord(
                eq(applicantId),
                contains(reason)
        );
    }
}