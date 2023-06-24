package gov.milove.controllers;

import gov.milove.domain.dto.NewsDTO;
import gov.milove.services.document.DocumentGroupService;
import gov.milove.services.NewsService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
@RequestMapping("/news")
public class News {

    private final NewsService newsService;
    private final DocumentGroupService documentGroupService;


    public News(NewsService newsService, DocumentGroupService documentGroupService) {
        this.newsService = newsService;
        this.documentGroupService = documentGroupService;
    }

    @GetMapping("/new")
    public String newNewsForm() {
        return "news/new";
    }

    @GetMapping("/all")
    public String newsAll(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page, Model model) {
        page = page-1;

        Page<NewsDTO> pages = newsService.getPagesList(page, 20);

        model.addAttribute("newsList", pages.toList());
        model.addAttribute("pagesQuantity", pages.getTotalPages());
        model.addAttribute("currentPage", page);
        return "news/all";
    }

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> newNews(@RequestParam("description") String description,
                          @RequestParam("mainText") String main_text,
                          @RequestParam("image") MultipartFile file,
                          @RequestParam(value = "date", required = false) String date ){

        try {
            newsService.saveNews(description, main_text, file, date);
            return new ResponseEntity<>("Створення успішне", HttpStatus.OK);
        } catch (ConstraintViolationException ex) {
            return new ResponseEntity<>("Вказано неправильні параметри", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteNews(@RequestParam("newsId") Long id) {
        boolean success = newsService.deleteNews(id);
        if (success) return new ResponseEntity<>("Видалення успішне", HttpStatus.OK);
        else return new ResponseEntity<>("Виникли проблеми з видаленням", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{news_id}")
    public String getFullNewsPage(@PathVariable("news_id") Long news_id, Model model) {
        Optional<gov.milove.domain.News> news = newsService.getNewsById(news_id);
        model.addAttribute("groups",documentGroupService.findAll());

        if (news.isEmpty()) {
            return "error/404";
        } else {
            model.addAttribute("news", news.get());
            return "news/page";
        }
    }


    @GetMapping("/edit")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String editNews(@RequestParam("newsId") Long news_id, Model model) {
        Optional<gov.milove.domain.News> news = newsService.getNewsById(news_id);

        if (news.isEmpty()) {
            return "error/404";
        } else {
            model.addAttribute("news", news.get());
            return "news/edit";
        }
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public  ResponseEntity<String> updateNews(@RequestParam(value = "description", required = false) String description,
                             @RequestParam("mainText") String main_text,
                             @RequestParam(value = "image", required = false) MultipartFile file,
                             @RequestParam("newsId") Long news_id) {

        newsService.updateNews(description, main_text, file, news_id);
        return new ResponseEntity<>("Оновлення успішне", HttpStatus.OK);
    }
}
