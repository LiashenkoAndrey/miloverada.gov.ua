package gov.milove.controllers;

import gov.milove.domain.News;
import gov.milove.services.document.DocumentGroupService;
import gov.milove.services.NewsService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Iterator;
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

        Page<News> pages = newsService.getPagesList(page);

        List<List<News>> dividedPages = new ArrayList<>();
        Iterator<News> iterator = pages.iterator();

        for (int i = 0; i < 3; i++) {
            List<News> newsList = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                if (iterator.hasNext()) {
                    newsList.add(iterator.next());
                }
            }
            dividedPages.add(newsList);
        }
        
        model.addAttribute("divided_pages", dividedPages);
        model.addAttribute("pagesQuantity", pages.getTotalPages());
        model.addAttribute("currentPage", page);
        return "main";
    }


}
