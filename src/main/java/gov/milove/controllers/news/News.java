package gov.milove.controllers.news;

import gov.milove.controllers.util.ControllerUtil;
import gov.milove.services.document.DocumentGroupService;
import gov.milove.services.NewsService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        model.addAttribute("groups",documentGroupService.findAll());

        Page<gov.milove.domain.News> pages = newsService.getPagesList(page, 20);

        model.addAttribute("newsList", pages.toList());
        model.addAttribute("pagesQuantity", pages.getTotalPages());
        model.addAttribute("currentPage", page);
        return "news/all";
    }

    @PostMapping("/new")
    public String newNews(@RequestParam("description") String description,
                          @RequestParam("main_text") String main_text,
                          @RequestParam("image") MultipartFile file,
                          @RequestParam("date") String date ){
        newsService.saveNews(description, main_text, file, date);

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
        Optional<gov.milove.domain.News> news = newsService.getNewsById(news_id);
        model.addAttribute("groups",documentGroupService.findAll());

        if (news.isEmpty()) {
            return "error/404";
        } else {
            model.addAttribute("news", news.get());
            return "news/page";
        }
    }


    @GetMapping("/{news_id}/edit")
    public String editNews(@PathVariable("news_id") Long news_id, Model model) {
        Optional<gov.milove.domain.News> news = newsService.getNewsById(news_id);

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
