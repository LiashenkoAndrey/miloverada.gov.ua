package gov.milove.controllers;

import gov.milove.domain.CustomDate;
import gov.milove.domain.News;
import gov.milove.services.NewsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/new")
    public String newNewsForm() {
        return "newNews";
    }

    @PostMapping("/new")
    public String newNews(@RequestParam("description") String description,
                          @RequestParam("main_text") String main_text) {

        News news =  new News();
        news.setDescription(description);
        news.setDate_of_creation(new CustomDate());
        news.setMain_text(main_text);
        news.setImage_id("63fa35d9287f9f5a3522057b");
        newsService.saveNews(news);

        return "redirect:/";
    }


}
