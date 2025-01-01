package gov.milove.domain.dto.media;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.loader.api.BeanMappingBuilder;
import gov.milove.domain.media.Banner;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import static com.github.dozermapper.core.loader.api.TypeMappingOptions.mapNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LinkBannerDto {

    private Long id;

    @NotNull
    @URL
    private String url;

    @Size(max = 2000)
    @NotNull
    private String text;

    public void mapToEntity(Banner banner) {
        Mapper mapper = DozerBeanMapperBuilder.create()
                .withMappingBuilder( new BeanMappingBuilder() {
                    protected void configure() {
                        mapping(
                                BannerDto.class,
                                Banner.class,
                                mapNull(false)
                        );
                    }
                }).build();
        mapper.map(this, banner);
    }
}
