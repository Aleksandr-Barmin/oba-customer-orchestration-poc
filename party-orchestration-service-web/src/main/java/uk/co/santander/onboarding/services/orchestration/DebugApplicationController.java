package uk.co.santander.onboarding.services.orchestration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.santander.onboarding.services.orchestration.model.ApplicationInfo;
import uk.co.santander.onboarding.services.orchestration.service.ApplicationRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class DebugApplicationController {
    @Autowired
    private ApplicationRepository repository;

    @GetMapping("/applications")
    public Map<UUID, List<ApplicationInfo>> applicationRecords() {
        return repository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        ApplicationInfo::getApplicationId
                ));
    }
}
