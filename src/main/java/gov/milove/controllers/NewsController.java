package gov.milove.controllers;

import gov.milove.domain.News;
import gov.milove.domain.dto.NewsDTO;
import gov.milove.domain.dto.NewsDtoWithImageAndType;
import gov.milove.exceptions.NewsNotFoundException;
import gov.milove.repositories.NewsRepository;
import gov.milove.repositories.NewsTypeRepository;
import gov.milove.services.NewsService;
import gov.milove.services.document.DocumentGroupService;
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

import java.util.List;

@Controller
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    private final NewsService newsService;
    private final NewsRepository newsRepository;
    private final NewsTypeRepository newsTypeRepository;
    private final DocumentGroupService documentGroupService;


    @GetMapping("/new")
    public String newNewsForm(Model model) {
        model.addAttribute("allTypesList", newsTypeRepository.getAllTypes());
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
    public ResponseEntity<String> newNews(@ModelAttribute NewsDtoWithImageAndType news){
        newsService.save(news);
        return new ResponseEntity<>("Створення успішне", HttpStatus.CREATED);
    }

    @GetMapping("/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteNews(@RequestParam("newsId") Long id) {
        newsService.deleteById(id);
        return new ResponseEntity<>("Видалення успішне", HttpStatus.OK);
    }

    @GetMapping("/{news_id}")
    public String getFullNewsPage(@PathVariable("news_id") Long news_id, Model model) {
        News news = newsRepository.findById(news_id).orElseThrow(NewsNotFoundException::new);
        List<NewsDTO> similarNews = (news.getNewsType() != null ?
                newsRepository.getLastNewsDTOByNewsTypeIdWithLimit(
                news_id,
                news.getNewsType().getId(),
                PageRequest.of(0, 3).withSort(Sort.Direction.DESC, "created")
                ).toList() : List.of());

        model.addAttribute("groups",documentGroupService.findAll());
        model.addAttribute("similarNews", similarNews);
        model.addAttribute("news", news);
        return "news/page";
    }

    @GetMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String editNews(@RequestParam("newsId") Long newsId, Model model) {
        News news = newsRepository.findById(newsId).orElseThrow(NewsNotFoundException::new);
        model.addAttribute("news", news);
        model.addAttribute("allTypesList", newsTypeRepository.getAllTypes());
        return "news/update";
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateNews(@ModelAttribute NewsDtoWithImageAndType news) {
        newsService.update(news);
        return new ResponseEntity<>("Оновлення успішне", HttpStatus.OK);
    }
}
