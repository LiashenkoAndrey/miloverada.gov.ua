package gov.milove.controllers;

import gov.milove.repositories.AboutRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

import static gov.milove.controllers.util.ControllerUtil.error;
import static gov.milove.controllers.util.ControllerUtil.ok;

@Controller
@RequestMapping("/about")
public class About {

    private final AboutRepo aboutRepo;

    public About(AboutRepo aboutRepo) {
        this.aboutRepo = aboutRepo;
    }

    @GetMapping
    public String getPage(Model model) {
        model.addAttribute("about", aboutRepo.findById(1L).orElseThrow(EntityNotFoundException::new));
        return "about/page";
    }

    @GetMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getUpdatePage(Model model) {
        model.addAttribute("about", aboutRepo.findById(1L).orElseThrow(EntityNotFoundException::new));
        return "about/edit";
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updatePage(@RequestParam("main_text") String main_text) {
        try {
            gov.milove.domain.About about = aboutRepo.findById(1L)
                    .orElseThrow(EntityNotFoundException::new);
            about.setLast_updated(LocalDateTime.now());
            about.setMain_text(main_text);
            aboutRepo.save(about);
            return ok("Сторінка успішно оновлена!");
        } catch (Exception ex) {
            return error("Виникли проблеми з оновленням");
        }
    }
}
