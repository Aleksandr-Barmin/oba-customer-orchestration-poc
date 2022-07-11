package uk.co.santander.onboarding.services.orchestration;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import uk.co.santander.onboarding.services.orchestration.model.ApplicationChannel;
import uk.co.santander.onboarding.services.orchestration.model.ApplicationStartRequest;
import uk.co.santander.onboarding.services.orchestration.model.CustomerType;
import uk.co.santander.onboarding.services.orchestration.service.OrchestrationService;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationState;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = {
        OrchestrationController.class
})
class OrchestrationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrchestrationService orchestrationService;

    @Test
    @DisplayName("Context should start")
    void check_contextStarts() {
        assertThat(mockMvc).isNotNull()
                .withFailMessage("Context should start");
    }

    @Test
    @DisplayName("Authorize - success should return 200")
    void authorize_success() throws Exception {
        when(orchestrationService.authorize(any(ApplicationStartRequest.class)))
                .thenReturn(OrchestrationState.CUSTOMER_READY_FOR_EXECUTE_STATE);

        final ApplicationStartRequest request = ApplicationStartRequest.builder()
                .applicantId(UUID.randomUUID())
                .channel(ApplicationChannel.MOBILE)
                .customerType(CustomerType.NEW_TO_BANK)
                .build();

        mockMvc.perform(
                post("/authorize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @EnumSource(
            value = OrchestrationState.class,
            mode = EnumSource.Mode.INCLUDE,
            names = {
                    "APPLICANT_DATA_VALIDATION_FAILED_STATE",
                    "CUSTOMER_FOUND_IN_BDP_STATE"
            }
    )
    void authorize_failure(OrchestrationState targetStated) throws Exception {
        when(orchestrationService.authorize(any(ApplicationStartRequest.class)))
                .thenReturn(targetStated);

        final ApplicationStartRequest request = ApplicationStartRequest.builder()
                .applicantId(UUID.randomUUID())
                .channel(ApplicationChannel.MOBILE)
                .customerType(CustomerType.NEW_TO_BANK)
                .build();

        mockMvc.perform(
                        post("/authorize")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void execute_success() throws Exception {
        when(orchestrationService.execute(any(ApplicationStartRequest.class)))
                .thenReturn(OrchestrationState.CUSTOMER_CREATED_STATE);

        final ApplicationStartRequest request = ApplicationStartRequest.builder()
                .applicantId(UUID.randomUUID())
                .channel(ApplicationChannel.MOBILE)
                .customerType(CustomerType.NEW_TO_BANK)
                .build();

        mockMvc.perform(
                        post("/execute")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());
    }
}