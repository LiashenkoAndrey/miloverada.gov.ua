package gov.milove.controllers;

import gov.milove.domain.News;
import gov.milove.domain.dto.NewsDTO;
import gov.milove.domain.dto.NewsDtoWithImageAndType;
import gov.milove.exceptions.NewsNotFoundException;
import gov.milove.repositories.NewsRepository;
import gov.milove.services.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;
    private final NewsRepository newsRepository;

    @GetMapping("/all")
    public List<NewsDTO> newsAll(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page, @RequestParam(value = "pageSize",required = false, defaultValue = "10") Integer size) {
        return newsService.getPagesList(page, size).toList();
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

    @GetMapping("/{newsId}")
    public News getFullNewsPage(@PathVariable("newsId") Long news_id) {
        return newsRepository.findById(news_id).orElseThrow(NewsNotFoundException::new);
    }

    @GetMapping("/{newsId}/similar")
    public List<NewsDTO> getSimilarNewsByNewsId(@PathVariable Long newsId) {
        News news = newsRepository.findById(newsId).orElseThrow(NewsNotFoundException::new);

        return (news.getNewsType() != null ?
                newsRepository.getLastNewsDTOByNewsTypeIdWithLimit(
                        newsId,
                        news.getNewsType().getId(),
                        PageRequest.of(0, 3).withSort(Sort.Direction.DESC, "created")
                ).toList() : List.of());
    }

    @GetMapping("/latest")
    public List<NewsDTO> getLatest(@RequestParam(defaultValue = "6", required = false) Integer pageSize) {
        return newsRepository.getLatest(PageRequest.ofSize(pageSize)).toList();
    }


    @PostMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateNews(@ModelAttribute NewsDtoWithImageAndType news) {
        newsService.update(news);
        return new ResponseEntity<>("Оновлення успішне", HttpStatus.OK);
    }

    @PostMapping("/{id}/incrementViews")
    public Long incrementViews(@PathVariable Long id) {
        newsRepository.incrementViews(id);
        return id;
    }
}
