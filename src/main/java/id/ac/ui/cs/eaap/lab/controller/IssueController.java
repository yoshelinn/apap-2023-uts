package id.ac.ui.cs.eaap.lab.controller;

import id.ac.ui.cs.eaap.lab.model.IssueModel;
import id.ac.ui.cs.eaap.lab.service.IssueService;
import id.ac.ui.cs.eaap.lab.service.ListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/lapor")
public class IssueController {

    @Autowired
    ListService listService;


    @GetMapping("/view-all")
    public String viewAllIssues(Model model) {
        // TODO
        log.info("view all issues");
        List<IssueModel> issueModels = new ArrayList<>();
        model.addAttribute("issueList", issueModels);
        return "issue/view-all";
    }


    @PostMapping(value = "/add", params = {"save"})
    public String addIssueSubmitPage(@ModelAttribute IssueModel issueModel, BindingResult result,
                                     RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            redirectAttrs.addFlashAttribute("error", "The error occurred.");
            return "redirect:/lapor/view-all";
        }

        // default values
        issueModel.setReportedOn(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
        issueModel.setStatus(listService.getStatusOptionsList().get(0));

        log.info(issueModel.toString());
        // TODO: store to database
        log.info("after save\n{}", issueModel);

        redirectAttrs.addFlashAttribute("success",
                String.format("Kasus berhasil disimpan dengan id %d", issueModel.getIssueId()));
        return "redirect:/lapor/view-all";
    }


    @GetMapping(value = "/{id}/update")
    public String updateStatus(@PathVariable Long id, Model model) {
        // TODO
        IssueModel issueModel = new IssueModel();
        model.addAttribute("issueModel", issueModel);
        model.addAttribute("listService", listService);

        return "issue/form-update-status";
    }

    @PostMapping(value = "/update", params = {"save"})
    public String updateStatus(@ModelAttribute IssueModel issueModel, BindingResult result,
                               RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            redirectAttrs.addFlashAttribute("error", "The error occurred.");
            return "redirect:/lapor/view-all";
        }

        // TODO: store to database

        redirectAttrs.addFlashAttribute("success",
                "Status berhasil di update");
        return "redirect:/lapor/view-all";
    }

    @GetMapping("/active")
    public String viewActiveIssues(Model model) {
        log.info("view all issues");
        return "issue/view-all";
    }
}
