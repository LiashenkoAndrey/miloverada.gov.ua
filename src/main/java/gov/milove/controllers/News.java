package gov.milove.controllers;

import gov.milove.domain.NewsType;
import gov.milove.domain.dto.NewsDTO;
import gov.milove.repositories.NewsRepository;
import gov.milove.services.document.DocumentGroupService;
import gov.milove.services.NewsService;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/news")
@RequiredArgsConstructor
public class News {

    private static final Logger logger = LoggerFactory.getLogger(News.class);

    private final NewsService newsService;

    private final NewsRepository newsRepository;
    private final DocumentGroupService documentGroupService;


    @GetMapping("/new")
    public String newNewsForm(Model model) {
        model.addAttribute("allTypesList", newsRepository.getAllTypes());
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
                          @RequestParam(value = "date", required = false) String date,
                          @RequestParam(value = "newsTypeId", required = false) Long newsTypeId,
                          @RequestParam(value = "typeTitle", required = false) String newsTypeTitle,
                          @RequestParam(value = "titleExplanation", required = false) String titleExplanation){

        try {
            if (newsTypeTitle != null && titleExplanation != null) {
                Long id = newsService.createNewsType(newsTypeTitle, titleExplanation);
                newsService.saveNews(description, main_text, file, date, id);
            } else {

                newsService.saveNews(description, main_text, file, date, newsTypeId);
            }


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
        Optional<gov.milove.domain.News> newsOptional = newsService.getNewsById(news_id);

        if (newsOptional.isEmpty()) {
            return "error/404";
        } else {
            gov.milove.domain.News news = newsOptional.get();

            List<NewsDTO> similarNews = newsRepository.getLastNewsDTOByNewsTypeIdWithLimit(
                            news_id,
                            news.getNews_type_id(),
                            PageRequest.of(0, 3).withSort(Sort.Direction.DESC, "created")
                    ).toList();

            model.addAttribute("groups",documentGroupService.findAll());
            model.addAttribute("similarNews", similarNews);
            model.addAttribute("news", news);
            return "news/page";
        }
    }


    @GetMapping("/edit")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String editNews(@RequestParam("newsId") Long news_id, Model model) {
        Optional<gov.milove.domain.News> newsOptional = newsService.getNewsById(news_id);

        if (newsOptional.isEmpty()) {
            return "error/404";
        } else {
            gov.milove.domain.News news = newsOptional.get();
            model.addAttribute("news", news);
            model.addAttribute("newsType", newsRepository.getNewsTypeById(news.getNews_type_id()));

            model.addAttribute("allTypesList", newsService.getAllTypes(news.getNews_type_id()));
            return "news/edit";
        }
    }


    @PostMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public  ResponseEntity<String> updateNews(@RequestParam(value = "description", required = false) String description,
                             @RequestParam("mainText") String main_text,
                             @RequestParam(value = "image", required = false) MultipartFile file,
                             @RequestParam("newsId") Long news_id,
                             @RequestParam(value = "newsTypeId", required = false) Long newsTypeId,
                             @RequestParam(value = "typeTitle", required = false) String newsTypeTitle,
                             @RequestParam(value = "titleExplanation", required = false) String titleExplanation) {

        newsService.updateNews(description, main_text, file, news_id, newsTypeId,  newsTypeTitle, titleExplanation);
        return new ResponseEntity<>("Оновлення успішне", HttpStatus.OK);
    }
}
