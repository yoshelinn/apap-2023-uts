package id.ac.ui.cs.eaap.lab.controller;


import id.ac.ui.cs.eaap.lab.service.IssueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/lapor")
public class IssueRestController {


    @GetMapping("/active")
    public ResponseEntity getActiveIssues() {
        log.info("api get active issues");
        // TODO
        return ResponseEntity.ok("");
    }

    @GetMapping("/statistics")
    public ResponseEntity getStatisticsIssues() {
        log.info("api statistics");
        // TODO
        return ResponseEntity.ok("");
    }
}
