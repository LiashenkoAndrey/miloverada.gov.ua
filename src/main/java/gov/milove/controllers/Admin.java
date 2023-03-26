package gov.milove.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for administrator
 */
@Controller
@RequestMapping("/admin")
public class Admin {

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
