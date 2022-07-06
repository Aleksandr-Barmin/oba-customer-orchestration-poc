package uk.co.santander.onboarding.services.orchestration.state.guard;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        StateContextHelper.class,
        ApplicantDataValidatedGuard.class
})
class ApplicantDataValidatedGuardTest {
    @Autowired
    private ApplicantDataValidatedGuard uut;

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
    @DisplayName("Should return false when value not in context")
    void evaluate_shouldReturnFalseWhenNotPresent() {
        when(context.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(Map.of());

        final boolean result = uut.evaluate(context);

        assertThat(result)
                .isFalse()
                .withFailMessage("Should return false when no value in context");

        verify(context, atLeastOnce()).getExtendedState();
        verify(extendedState, atLeastOnce()).getVariables();
    }

    @ParameterizedTest(name = "Checking {0}")
    @EnumSource(ApplicantValidationStatus.class)
    void evaluate_shouldReturnSavedValue(final ApplicantValidationStatus savedResult) {
        when(context.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(Map.of(
                StateConstants.APPLICANT_VALIDATION_STATUS,
                savedResult
        ));

        final boolean result = uut.evaluate(context);

        assertThat(result)
                .isEqualTo(savedResult.isPositive())
                .withFailMessage("Invalid result");

        verify(context, atLeastOnce()).getExtendedState();
        verify(extendedState, atLeastOnce()).getVariables();
    }
}