package gov.milove.controllers.media;

import gov.milove.domain.news.News;
import gov.milove.domain.news.NewsImage;
import gov.milove.domain.dto.news.NewsPageDto;
import gov.milove.domain.news.NewsType;
import gov.milove.domain.dto.news.INewsDto;
import gov.milove.domain.dto.news.NewsDtoWithImageAndType;
import gov.milove.exceptions.IllegalParameterException;
import gov.milove.exceptions.NewsNotFoundException;
import gov.milove.repositories.jpa.news.NewsRepository;
import gov.milove.repositories.jpa.news.NewsTypeRepo;
import gov.milove.services.news.NewsImagesService;
import gov.milove.services.news.NewsService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
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
    public List<INewsDto> newsAll(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                  @RequestParam(value = "pageSize",required = false, defaultValue = "10") Integer size) {

        return newsRepository.findDistinctBy(PageRequest.of(page, size)
                .withSort(Sort.Direction.DESC, "dateOfPublication" )).toList();

    }

    @PostMapping("/protected/newsType/new")
    public NewsType saveNewsType(@Validated @RequestBody NewsType newsType) {
        return newsTypeRepo.save(newsType);
    }



//    @GetMapping("/documents/deleteUnused")
//    public void delete() {
//        log.info("Start search");
//        List<String> documents = newsRepository.findAll().stream().map(News::getImage_id).toList();
//        log.info("50%");
//        List<MongoDocument> mongoDocuments = mongoDocumentRepo.findAll().stream()
//                .filter(mongoDocument -> !documents.contains(mongoDocument.getId())).toList();
//        log.info("Not used docs! size = {} {}", mongoDocuments.size(), mongoDocuments);
//        mongoDocumentRepo.deleteAll(mongoDocuments);
//        log.info("End search");
//    }

    @PostMapping("/protected/news/new")
    @Transactional
    public ResponseEntity<Long> newNews(@RequestParam @NotBlank @Size(max = 300) String title,
                                          @RequestParam @NotBlank String text,
                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateOfPublication,
                                          @RequestParam(required = false) @Future @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateOfPostponedPublication,
                                          @RequestParam @NotEmpty @Size(max = 20) MultipartFile[] images,
                                          @RequestParam Long newsTypeId
    ){
        log.info("CREATE NEWS");
        log.info("images length = = {}", images.length);
        log.info("title = {}, dateOfPublication = {}, dateOfPostponedPublication = {}", title, dateOfPublication, dateOfPostponedPublication);
        log.info("newsType = {}", newsTypeId);

        NewsType newsType = newsTypeId > 0 ? newsTypeRepo.findById(newsTypeId).orElseThrow(EntityNotFoundException::new) : null;

        News newNews = News.builder()
                .description(title)
                .main_text(text)
                .dateOfPublication(dateOfPublication)
                .newsType(newsType)
                .views(0L)
                .build();

        News news = newsService.save(newNews, images, dateOfPostponedPublication);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(news.getId());
    }

    @DeleteMapping("/protected/newsType/{id}/delete")
    public void deleteNewsTypeById(@PathVariable Long id) {
        if (id <= 0) throw new IllegalParameterException("Id must be higher than zero");
        newsTypeRepo.deleteById(id);
    }



    @GetMapping("/protected/news-types")
    public List<NewsType> getNewsTypes() {
        return newsTypeRepo.findAll();
    }

    @DeleteMapping("/protected/news/{id}/delete")
    public ResponseEntity<?> deleteNewsById(@PathVariable Long id) {
        if (id <= 0) throw new IllegalParameterException("Id must be higher than zero");
        newsService.deleteById(id);
        return ResponseEntity.accepted().body(id);
    }

    @DeleteMapping("/protected/news/image/{id}/delete")
    public ResponseEntity<String> deleteNewsImageById(@PathVariable String id) {
        if (!ObjectId.isValid(id)) throw new IllegalParameterException("Image id hex string is not valid");
        newsService.deleteNewsImageById(id);
        return ResponseEntity.accepted().body(id);
    }

    @PutMapping("/protected/news/{id}/update")
    public ResponseEntity<Long> updateNews(@PathVariable Long id,
                           @RequestParam @NotBlank @Size(max = 300) String title,
                           @RequestParam @NotBlank  String text,
                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateOfPublication) {
        log.info("title = {}, text = {}, date = {}", title, text, dateOfPublication);
        News news = newsRepository.findById(id).orElseThrow(NewsNotFoundException::new);
        news.setDescription(title);
        news.setDateOfPublication(dateOfPublication);
        news.setMain_text(text);
        newsRepository.save(news);
        return ResponseEntity
                .accepted()
                .body(id);
    }

    @PostMapping("/protected/news/{newsId}/image/new")
    public List<NewsImage> saveNewNewsImage(@PathVariable Long newsId,
                                            @RequestParam("images") MultipartFile[] files) {
        News news = newsRepository.findById(newsId).orElseThrow(NewsNotFoundException::new);
        List<NewsImage> newsImages = newsImagesService.saveAll(List.of(files));
        news.getImages().addAll(newsImages);
        newsRepository.save(news);
        return newsImages;
    }

    @GetMapping("/news/{newsId}")
    public News getNewsById(@PathVariable("newsId") Long news_id) {
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
    public NewsPageDto getLatest(@RequestParam(defaultValue = "6", required = false) @Min(1) Integer pageSize,
                                 @RequestParam(defaultValue = "0", required = false) Integer pageNumber) {
        Page<INewsDto> newsDtos = newsRepository.findDistinctBy(PageRequest.ofSize(pageSize).withPage(pageNumber).withSort(Sort.Direction.DESC, "dateOfPublication" ));
        return new NewsPageDto(newsDtos);
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
