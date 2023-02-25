package gov.milove.controllers;

import gov.milove.domain.News;
import gov.milove.services.DocumentGroupService;
import gov.milove.services.NewsService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {

    private final DocumentGroupService documentGroupService;
    private final NewsService newsService;
    public MainController(DocumentGroupService documentGroupService, NewsService newsService) {
        this.documentGroupService = documentGroupService;
        this.newsService = newsService;
    }

    @GetMapping("/")
    public String main(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page, Model model) {
        page = page-1;
        model.addAttribute("groups",documentGroupService.findAll());
        List<Page<News>> pagesList = newsService.getPagesList(page);
        model.addAttribute("news_page_list", pagesList);
        model.addAttribute("pagesQuantity", pagesList.get(0).getTotalPages());
        model.addAttribute("currentPage", page);
        return "main";
    }


}
