package gov.milove.controllers;

import gov.milove.domain.News;
import gov.milove.services.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;


    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/new")
    public String newNewsForm() {
        return "news/new";
    }

    @PostMapping("/new")
    public String newNews(@RequestParam("description") String description,
                          @RequestParam("main_text") String main_text,
                          @RequestParam("image") MultipartFile file) {
        newsService.saveNews(description, main_text, file);

        return "redirect:/";
    }

    @GetMapping("/{news_id}/delete")
    public ResponseEntity<String> deleteNews(@PathVariable("news_id") Long id) {
        boolean success = newsService.deleteNews(id);
        if (success) return new ResponseEntity<>("Видалення успішне", HttpStatus.OK);
        else return new ResponseEntity<>("Виникли проблеми з видаленням", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{news_id}")
    public String getFullNewsPage(@PathVariable("news_id") Long news_id, Model model) {
        Optional<News> news = newsService.getNewsById(news_id);

        if (news.isEmpty()) {
            return "error/404";
        } else {
            model.addAttribute("news", news.get());
            return "news/page";
        }
    }


    @GetMapping("/{news_id}/edit")
    public String editNews(@PathVariable("news_id") Long news_id, Model model) {
        Optional<News> news = newsService.getNewsById(news_id);

        if (news.isEmpty()) {
            return "error/404";
        } else {
            model.addAttribute("news", news.get());
            return "news/edit";
        }
    }

    @PostMapping("/{news_id}/update")
    public String updateNews(@RequestParam("description") String description,
                             @RequestParam("main_text") String main_text,
                             @RequestParam("image") MultipartFile file,
                             @PathVariable("news_id") Long news_id) {

        newsService.updateNews(description, main_text, file, news_id);
        return "redirect:/news/" + news_id;
    }
}
