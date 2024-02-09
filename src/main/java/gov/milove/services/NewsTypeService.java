package gov.milove.services;

import gov.milove.domain.NewsType;
import gov.milove.repositories.NewsTypeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsTypeService {

    private final NewsTypeRepo newsTypeRepo;

//    public Long createNewsType(String title, String explanation) {
//        newsTypeRepo.createNewsType(title, explanation);
//        return newsTypeRepo.getLastInsertedIdOfNewsType();
//    }
//
//    public List<NewsType> getAllTypes(Long newsTypeId) {
//
//        List<NewsType> newsTypes = newsTypeRepo.findAll();
//        if (newsTypeId != null) {
//            // find type of edited news and swap with element with 0 index
//            int i = 0; //news id
//            while (!newsTypes.get(i).getId().equals(newsTypeId)) i++;
//
//            // swap with element with 0 index
//            NewsType temp = newsTypes.get(0);
//            newsTypes.set(0, newsTypes.get(i));
//            newsTypes.set(i, temp);
//        }
//        return newsTypes;
//    }
}
