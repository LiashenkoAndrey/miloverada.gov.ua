package gov.milove.controllers.media;

import gov.milove.domain.media.TextBanner;
import gov.milove.domain.dto.document.media.TextBannerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static gov.milove.util.EntityMapper.map;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/text-banner")
@RequiredArgsConstructor
public class TextBannerController {

    private final TextBannerRepository repo;

    @GetMapping("/all")
    public List<TextBanner> getAll() {
        return repo.findAll(Sort.by("createdOn").descending());
    }

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


    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody TextBanner banner) {
        if (banner.getId() == null) return ResponseEntity.badRequest().build();
        TextBanner saved = repo.findById(banner.getId()).orElseThrow(EntityNotFoundException::new);

        map(banner, saved)
                .mapEmptyString(false)
                .mapNull(false)
                .map();

        repo.save(saved);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("id") Long id) {
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
