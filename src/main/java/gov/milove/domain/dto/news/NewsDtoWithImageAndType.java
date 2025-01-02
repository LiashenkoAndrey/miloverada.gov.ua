package gov.milove.domain.dto.news;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.loader.api.BeanMappingBuilder;
import com.github.dozermapper.core.loader.api.TypeMappingOptions;
import gov.milove.domain.news.News;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import static com.github.dozermapper.core.loader.api.FieldsMappingOptions.customConverter;


@Getter
@ToString
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsDtoWithImageAndType {

    private Long id;

    private String description;

    private String main_text;

    private String created;

    private MultipartFile image;

    private String typeTitle;

    private String titleExplanation;

    private String news_type_id;

    public void mapToEntity(News news) {
        Mapper mapper = DozerBeanMapperBuilder.create()
                .withMappingBuilder( new BeanMappingBuilder() {
                    protected void configure() {
                        mapping(
                                NewsDtoWithImageAndType.class,
                                News.class,
                                TypeMappingOptions.mapEmptyString(false)
                        ).fields(
                                "created",
                                "created",
                                customConverter("gov.milove.util.StringToLocalDateConverter")
                        ).exclude("news_type_id");
                    }
                })
                .build();

        mapper.map(this, news);
    }

    public News toEntity() {
        News news = new News();
        mapToEntity(news);
        return news;
    }


}
