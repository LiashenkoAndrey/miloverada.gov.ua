package gov.milove.controllers;

import gov.milove.domain.Banner;
import gov.milove.repositories.BannerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/banner")
@RequiredArgsConstructor
public class BannerController {

    private final BannerRepository bannerRepository;

    @GetMapping("/new")
    public String create() {
        return "/banner/new";
    }

    @PostMapping("/new")
    public ResponseEntity<Long> createBanner(@RequestBody Banner banner) {
        if (bannerRepository.exists(Example.of(banner))) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        } else {
            Banner saved = bannerRepository.save(banner);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(saved.getId());
        }
    }

    @GetMapping("/update")
    public String update(@RequestParam("id") Long id, Model model) {
        Banner banner = bannerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        model.addAttribute("banner", banner);
        return "/banner/update";
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateBanner(@RequestBody Banner banner) {
        if (banner.getId() == null) {
            return ResponseEntity.badRequest().build();
        } else {
            bannerRepository.save(banner);
            return ResponseEntity.ok().build();
        }
    }

    @DeleteMapping("/delete")
    public void delete() {

    }
}
