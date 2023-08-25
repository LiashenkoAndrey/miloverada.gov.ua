package gov.milove.controllers;

import gov.milove.domain.Banner;
import gov.milove.domain.dto.BannerDto;
import gov.milove.repositories.BannerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
            banner.setCreatedOn(LocalDate.now());
            Banner saved = bannerRepository.save(banner);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(saved.getId());
        }
    }

    @GetMapping("/view")
    public String page(@RequestParam("id") Long id, Model model) {
        Banner banner = bannerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        model.addAttribute("selectedBanner", banner);
        model.addAttribute("banners", bannerRepository.findAll());
        return "/banner/page";
    }

    @GetMapping("/update")
    public String update(@RequestParam("id") Long id, Model model) {
        Banner banner = bannerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        model.addAttribute("banner", banner);
        return "/banner/update";
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateBanner(@RequestBody BannerDto dto) {
        if (dto.getId() == null) return ResponseEntity.badRequest().build();
        Banner saved = bannerRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);
        saved.setLastUpdated(LocalDateTime.now());
        dto.mapToEntity(saved);
        bannerRepository.save(saved);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("id") Long id) {
        bannerRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
