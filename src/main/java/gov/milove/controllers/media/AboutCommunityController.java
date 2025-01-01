package gov.milove.controllers.media;

import gov.milove.domain.media.About;
import gov.milove.repositories.mongo.AboutRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AboutCommunityController {

    private final AboutRepo aboutRepo;

    @GetMapping("/aboutCommunity")
    public About getPage() {
        return aboutRepo.findById("663f6e32388652544c7ff08f").orElseThrow(EntityNotFoundException::new);
    }

    @PutMapping("/protected/aboutCommunity/update")
    public String getUpdatePage(@RequestParam String mainText) {
        About about =  aboutRepo.findById("663f6e32388652544c7ff08f").orElseThrow(EntityNotFoundException::new);
        about.setMainText(mainText);
        aboutRepo.save(about);
        return about.getId();
    }

//    @PostMapping("/update")
//    public ResponseEntity<String> updatePage(@RequestParam("main_text") String main_text) {
//        try {
//            gov.milove.domain.About about = aboutRepo.findById(1L)
//                    .orElseThrow(EntityNotFoundException::new);
//            about.setLast_updated(LocalDateTime.now());
//            about.setMain_text(main_text);
//            aboutRepo.save(about);
//            return ok("Сторінка успішно оновлена!");
//        } catch (Exception ex) {
//            return error("Виникли проблеми з оновленням");
//        }
//    }
}
