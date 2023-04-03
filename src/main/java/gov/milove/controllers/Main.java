package gov.milove.controllers;

import gov.milove.controllers.util.ControllerUtil;
import gov.milove.domain.News;
import gov.milove.repositories.document.DocumentGroupRepository;
import gov.milove.services.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class Main {

    private final NewsService newsService;

    private final DocumentGroupRepository docGroupRepo;

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("groups", docGroupRepo.findGeneralGroupsDto());

        // get last 9 news
        Page<News> pages = newsService.getPagesList(0, 9);
        model.addAttribute("divided_pages", ControllerUtil.packageNews(pages,9));
        return "main";
    }


}
