package gov.milove.controllers.digitalQueue;

import gov.milove.domain.digitalQueue.Service;
import gov.milove.repositories.ImageRepository;
import gov.milove.services.TerritorialCommunityServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/services")
public class ServiceController {

    private final TerritorialCommunityServiceService service;

    private final ImageRepository imageRepository;

    @GetMapping("/all")
    public String showAll(Model model) {
        model.addAttribute("services", service.findAll());
        return "digitalQueue/services";
    }

    @PostMapping("/new")
    public ResponseEntity<String> newService(@RequestParam("name") String name,
                                     @RequestParam("description") String description,
                                     @RequestParam("image") MultipartFile imageFile) {

        String imageId = imageRepository.saveImage(imageFile);

        service.save(new Service(name, description, imageId));
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/manage")
    public String manage(Model model) {
        model.addAttribute("services", service.findAll());
        return "digitalQueue/manage";
    }
}
