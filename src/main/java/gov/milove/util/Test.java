package gov.milove.util;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.loader.api.BeanMappingBuilder;
import com.github.dozermapper.core.loader.api.TypeMappingOptions;
import gov.milove.domain.News;
import gov.milove.domain.dto.NewsDtoWithImageAndType;

import java.time.LocalDate;

import static com.github.dozermapper.core.loader.api.FieldsMappingOptions.customConverter;


public class Test {
    public static void main(String[] args) {
        System.out.println(Long.parseLong(""));
//        NewsDtoWithImageAndType a  = new NewsDtoWithImageAndType();
//        a.setNews_type_id(null);
//        a.setMain_text("sdfds");
//        a.setDescription("");
//        a.setCreated("2003-11-12");
//
//        Mapper mapper = DozerBeanMapperBuilder.create()
//                .withMappingBuilder( new BeanMappingBuilder() {
//                    protected void configure() {
//                        mapping(NewsDtoWithImageAndType.class, News.class, TypeMappingOptions.mapEmptyString(false))
//                                .fields(
//                                        "created",
//                                        "created",
//                                        customConverter("gov.milove.util.StringToLocalDateConverter")
//                                );
//                    }
//                })
//                .withCustomConverter(new MapNullStringToNull())
//                .build();
//
//        News b = new News();
//        b.setNews_type_id(4L);
//        b.setCreated(LocalDate.now());
//        System.out.println(a);
//        System.out.println(b);
//        mapper.map(a, b);
//        System.out.println(b);

    }
}
