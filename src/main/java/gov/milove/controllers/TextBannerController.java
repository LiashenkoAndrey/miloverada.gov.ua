package gov.milove.controllers;

import gov.milove.domain.TextBanner;
import gov.milove.repositories.TextBannerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static gov.milove.util.EntityMapper.map;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;

@Controller
@RequestMapping("/text-banner")
@RequiredArgsConstructor
public class TextBannerController {

    private final TextBannerRepository repo;

    @PostMapping("/new")
    public ResponseEntity<Long> createBanner(@RequestBody TextBanner banner) {
        if (repo.exists(Example.of(banner))) {
            return ResponseEntity
                    .status(CONFLICT)
                    .build();
        } else {
            TextBanner saved = repo.save(banner);
            return ResponseEntity
                    .status(CREATED)
                    .body(saved.getId());
        }
    }

    @GetMapping("/new")
    public String newBanner() {
        return "/textBanner/new";
    }

    @GetMapping("/view")
    public String page(@RequestParam("id") Long id, Model model) {
        TextBanner textBanner = repo.findById(id).orElseThrow(EntityNotFoundException::new);
        model.addAttribute("selectedBanner", textBanner);
        model.addAttribute("banners", repo.findAll());
        return "/textBanner/page";
    }

    @GetMapping("/update")
    public String update(@RequestParam("id") Long id, Model model) {
        TextBanner textBanner = repo.findById(id).orElseThrow(EntityNotFoundException::new);
        model.addAttribute("banner", textBanner);
        return "/textBanner/update";
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody TextBanner banner) {
        if (banner.getId() == null) return ResponseEntity.badRequest().build();
        TextBanner saved = repo.findById(banner.getId()).orElseThrow(EntityNotFoundException::new);
        System.out.println(banner);
        System.out.println(saved);
        map(banner, saved)
                .mapEmptyString(false)
                .mapNull(false)
                .map();
        System.out.println(banner);
        System.out.println(saved);
        repo.save(saved);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("id") Long id) {
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
