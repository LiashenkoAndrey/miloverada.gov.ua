package gov.milove.domain.dto;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.loader.api.BeanMappingBuilder;
import com.github.dozermapper.core.loader.api.TypeMappingOptions;
import gov.milove.domain.Banner;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BannerDto {

    private Long id;

    @Size(min = 4, max = 120)
    @NotNull
    private String description;

    @Size(max = 40000)
    @NotNull
    private String mainText;

    public void mapToEntity(Banner banner) {
        Mapper mapper = DozerBeanMapperBuilder.create()
                .withMappingBuilder( new BeanMappingBuilder() {
                    protected void configure() {
                        mapping(
                                BannerDto.class,
                                Banner.class,
                                TypeMappingOptions.mapNull(false)
                        );
                    }
                }).build();
        mapper.map(this, banner);
    }
}
