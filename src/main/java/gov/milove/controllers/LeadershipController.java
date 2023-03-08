package gov.milove.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LeadershipController {

    @GetMapping("/управління")
    public String getPage() {
        return "leadership";
    }

    @GetMapping("/депутати")
    public String getDeputiesPage() {
        return "deputies";
    }
}
