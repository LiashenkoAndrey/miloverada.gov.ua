package gov.milove.controllers;

import gov.milove.domain.dto.NewsDTO;
import gov.milove.repositories.BannerRepository;
import gov.milove.repositories.LinkBannerRepository;
import gov.milove.repositories.TextBannerRepository;
import gov.milove.repositories.document.DocumentGroupRepository;
import gov.milove.services.impl.NewsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class Main {

    private final NewsServiceImpl newsService;

    private final DocumentGroupRepository docGroupRepo;

    private final TextBannerRepository textBannerRepository;

    private final LinkBannerRepository linkBannerRepository;

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("groups", docGroupRepo.findGeneralGroupsDto());

        Page<NewsDTO> pages = newsService.getPagesList(0, 9);
        model.addAttribute("newsList", pages.toList());
        model.addAttribute("textBanners", textBannerRepository.findAll());
        model.addAttribute("linkBanners", linkBannerRepository.findAll());
        return "main";
    }

}
