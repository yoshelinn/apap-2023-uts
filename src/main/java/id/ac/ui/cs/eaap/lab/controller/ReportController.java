package id.ac.ui.cs.eaap.lab.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/report")
public class ReportController {

    @GetMapping(value = "/active")
    public String viewActiveIssueCase(Model model) {
        return "report/report-active-issues";
    }

    @GetMapping(value = "/statistics")
    public String viewStatistics(Model model) {
        return "report/report-statistics";
    }

}
