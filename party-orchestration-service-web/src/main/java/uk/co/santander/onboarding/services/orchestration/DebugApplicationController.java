package uk.co.santander.onboarding.services.orchestration;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import uk.co.santander.onboarding.services.orchestration.model.ApplicationInfo;
import uk.co.santander.onboarding.services.orchestration.model.ApplicationInfoRecord;
import uk.co.santander.onboarding.services.orchestration.service.ApplicationRepository;

/**
 * Web MVC controller that provides debugging information regarding applications.
 */
@Controller
public class DebugApplicationController {
  @Autowired private ApplicationRepository repository;

  /**
   * Render a view with list of application events.
   *
   * @param modelAndView provided by Spring.
   * @return model and view to render.
   */
  @GetMapping("/web/applications")
  public ModelAndView applicationRecords(final ModelAndView modelAndView) {
    modelAndView.setViewName("applications");

    final List<ApplicationInfoRecord> records =
        repository.findAll().stream()
            .collect(Collectors.groupingBy(ApplicationInfo::getApplicationId))
            .entrySet()
            .stream()
            .map(
                entry ->
                    ApplicationInfoRecord.builder()
                        .applicationId(entry.getKey().toString())
                        .records(entry.getValue())
                        .build())
            .collect(Collectors.toList());

    modelAndView.addObject("records", records);
    return modelAndView;
  }
}
