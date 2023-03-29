package gov.milove.controllers;

import gov.milove.controllers.util.ControllerUtil;
import gov.milove.domain.News;
import gov.milove.services.document.DocumentGroupService;
import gov.milove.services.NewsService;
import jakarta.annotation.security.PermitAll;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class Main {

    private final DocumentGroupService documentGroupService;
    private final NewsService newsService;
    public Main(DocumentGroupService documentGroupService, NewsService newsService) {
        this.documentGroupService = documentGroupService;
        this.newsService = newsService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("groups",documentGroupService.findAll());

        // get last 9 news
        Page<News> pages = newsService.getPagesList(0, 9);
        model.addAttribute("divided_pages", ControllerUtil.packageNews(pages,9));
        return "main";
    }


}
