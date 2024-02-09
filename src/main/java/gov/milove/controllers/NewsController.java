package gov.milove.controllers;

import gov.milove.domain.News;
import gov.milove.domain.NewsImage;
import gov.milove.domain.NewsType;
import gov.milove.domain.dto.INewsDto;
import gov.milove.domain.dto.NewsDtoWithImageAndType;
import gov.milove.exceptions.NewsNotFoundException;
import gov.milove.repositories.NewsRepository;
import gov.milove.repositories.NewsTypeRepo;
import gov.milove.services.NewsImagesService;
import gov.milove.services.NewsService;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Log4j2
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class NewsController {

    private final NewsService newsService;
    private final NewsRepository newsRepository;
    private final NewsTypeRepo newsTypeRepo;
    private final NewsImagesService newsImagesService;

    @GetMapping("/news/all")
    public List<INewsDto> newsAll(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page, @RequestParam(value = "pageSize",required = false, defaultValue = "10") Integer size) {
        return newsRepository.findDistinctBy(PageRequest.of(page, size).withSort(Sort.Direction.DESC, "dateOfPublication" ));

    }

    @PostMapping("/protected/newsType/new")
    public NewsType saveNewsType(@Validated @RequestBody NewsType newsType) {
        return newsTypeRepo.save(newsType);
    }
    @PostMapping("/protected/news/new")
    public ResponseEntity<Long> newNews(@RequestParam @NotBlank @Size(max = 300) String title,
                                          @RequestParam @NotBlank @Size(max = 10000) String text,
                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateOfPublication,
                                          @RequestParam(required = false) @Future @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateOfPostponedPublication,
                                          @RequestParam @NotEmpty @Size(max = 20) MultipartFile[] images,
                                          @RequestParam Long newsTypeId
    ){
        log.info("images length = = {}", images.length);
        log.info("title = {}, text = {}, dateOfPublication = {}, dateOfPostponedPublication = {}", title, text, dateOfPublication, dateOfPostponedPublication);
        log.info("newsType = {}", newsTypeId);

        News newNews = News.builder()
                .description(title)
                .main_text(text)
                .dateOfPublication(dateOfPublication)
                .newsType(newsTypeRepo.getReferenceById(newsTypeId))
                .views(0L)
                .build();

        News news = newsService.save(newNews, images, dateOfPostponedPublication);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(news.getId());
    }

    @DeleteMapping("/protected/newsType/{id}/delete")
    public void deleteNewsTypeById(@PathVariable Long id) {
        newsTypeRepo.deleteById(id);
    }



    @GetMapping("/protected/news-types")
    public List<NewsType> getNewsTypes() {
        return newsTypeRepo.findAll();
    }

    @DeleteMapping("/protected/news/{id}/delete")
    public ResponseEntity<?> deleteNewsById(@PathVariable Long id) {
        newsService.deleteById(id);

        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/protected/news/image/{id}/delete")
    public ResponseEntity<String> deleteNewsImageById(@PathVariable String id) {
        newsService.deleteNewsImageById(id);

        return ResponseEntity

                .accepted()
                .body(id);
    }

    @PostMapping("/protected/news/{newsId}/image/new")
    public List<String> saveNewNewsImage(@PathVariable Long newsId, @RequestParam("images") MultipartFile[] files) {
        News news = newsRepository.findById(newsId).orElseThrow(NewsNotFoundException::new);
        List<NewsImage> newsImages = newsImagesService.saveAll(List.of(files));
        news.getImages().addAll(newsImages);
        newsRepository.save(news);
        return newsImages.stream()
                .map((NewsImage::getMongoImageId))
                .toList();
    }


    @GetMapping("/news/{newsId}")
    public News getFullNewsPage(@PathVariable("newsId") Long news_id) {
        return newsRepository.findById(news_id).orElseThrow(NewsNotFoundException::new);
    }

    @GetMapping("/news/{newsId}/similar")
    public List<INewsDto> getSimilarNewsByNewsId(@PathVariable Long newsId) {
        News news = newsRepository.findById(newsId).orElseThrow(NewsNotFoundException::new);

        return (news.getNewsType() != null ?
                newsRepository.getLastNewsDTOByNewsTypeIdWithLimit(
                        newsId,
                        news.getNewsType().getId(),
                        PageRequest.of(0, 3).withSort(Sort.Direction.DESC, "dateOfPublication")
                ).toList() : List.of());
    }

    @GetMapping("/news/latest")
    public List<INewsDto> getLatest(@RequestParam(defaultValue = "6", required = false) Integer pageSize) {
        return newsRepository.findDistinctBy(PageRequest.ofSize(pageSize).withSort(Sort.Direction.DESC, "dateOfPublication" ));
    }


    @PostMapping("/news/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateNews(@ModelAttribute NewsDtoWithImageAndType news) {
        newsService.update(news);
        return new ResponseEntity<>("Оновлення успішне", HttpStatus.OK);
    }

    @PostMapping("/news/{id}/incrementViews")
    public Long incrementViews(@PathVariable Long id) {
        newsRepository.incrementViews(id);
        return id;
    }
}
