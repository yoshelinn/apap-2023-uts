package id.ac.ui.cs.eaap.lab.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class MainController {

    @GetMapping(value = "/")
    public String getHome(Model model) {
        log.info("access home");
        return "home";
    }
}
