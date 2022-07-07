package uk.co.santander.onboarding.services.orchestration.state.guard;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.co.santander.onboarding.core.client.search.CustomerSearchStatus;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateConstants;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateContextHelper;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        StateContextHelper.class,
        CustomerNotFoundInBDPGuard.class
})
class CustomerNotFoundInBDPGuardTest {
    @Autowired
    private CustomerNotFoundInBDPGuard uut;

    @Mock
    private StateContext context;

    @Mock
    private ExtendedState extendedState;

    @ParameterizedTest
    @EnumSource(CustomerSearchStatus.class)
    void evaluate_shouldReturnTrueIfAllowed(final CustomerSearchStatus savedStatus) {
        when(context.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(Map.of(
                StateConstants.CORE_SEARCH_STATUS,
                savedStatus
        ));

        final boolean result = uut.evaluate(context);

        assertThat(result)
                .isNotEqualTo(savedStatus.isFound())
                .withFailMessage("Invalid evaluation");
    }
}