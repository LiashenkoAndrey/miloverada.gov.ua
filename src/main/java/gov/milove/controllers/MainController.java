package gov.milove.controllers;

import gov.milove.services.DocumentGroupService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private final DocumentGroupService documentGroupService;

    public MainController(DocumentGroupService documentGroupService) {
        this.documentGroupService = documentGroupService;
    }

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("groups",documentGroupService.findAll());
        return "main";
    }


}
