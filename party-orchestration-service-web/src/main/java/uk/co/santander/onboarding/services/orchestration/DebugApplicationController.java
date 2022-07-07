package uk.co.santander.onboarding.services.orchestration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import uk.co.santander.onboarding.services.orchestration.model.ApplicationInfo;
import uk.co.santander.onboarding.services.orchestration.model.ApplicationInfoRecord;
import uk.co.santander.onboarding.services.orchestration.service.ApplicationRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class DebugApplicationController {
    @Autowired
    private ApplicationRepository repository;

    @GetMapping("/web/applications")
    public ModelAndView applicationRecords(final ModelAndView modelAndView) {
        modelAndView.setViewName("applications");

        final List<ApplicationInfoRecord> records = repository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        ApplicationInfo::getApplicationId
                ))
                .entrySet()
                .stream()
                .map(entry -> ApplicationInfoRecord.builder()
                        .applicationId(entry.getKey().toString())
                        .records(entry.getValue())
                        .build())
                .collect(Collectors.toList());

        modelAndView.addObject("records", records);
        return modelAndView;
    }
}
