package gov.milove.services;

import gov.milove.domain.News;
import gov.milove.repositories.NewsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {

    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public List<Page<News>> getPagesList(int page) {
        int pointer;
        if (page == 1) {
            pointer = 0;
        } else {
            pointer = (page-1) * 3;
        }
        List<Page<News>> pageList = new ArrayList<>();
        for (pointer = page; pointer < 3; pointer++) {
            pageList.add(newsRepository.findAll(PageRequest.of(pointer,3)));
        }
        return pageList;
    }

    public void saveNews(News news) {
        newsRepository.save(news);
        System.out.println("News saved!!!");
    }
}
