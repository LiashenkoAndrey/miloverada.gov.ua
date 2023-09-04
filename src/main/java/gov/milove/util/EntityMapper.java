package gov.milove.util;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.loader.api.BeanMappingBuilder;
import com.github.dozermapper.core.loader.api.TypeMappingOption;
import com.github.dozermapper.core.loader.api.TypeMappingOptions;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class EntityMapper {

    public static <T1, T2> MapBuilder<T1, T2> map(T1 from, T2 to) {
        return new MapBuilder<>(from, to);
    }

    @NoArgsConstructor
    public static class MapBuilder<T1, T2> {

        T1 t1;
        T2 t2;

        private List<TypeMappingOption> optionsList = new ArrayList<>();

        public MapBuilder(T1 t1, T2 t2) {
            this.t1 = t1;
            this.t2 = t2;
        }

        public MapBuilder<T1, T2> mapNull(boolean mapNull) {
            optionsList.add(TypeMappingOptions.mapNull(mapNull));
            return this;
        }

        public MapBuilder<T1, T2> mapEmptyString(boolean mapEmptyString) {
            optionsList.add(TypeMappingOptions.mapEmptyString(mapEmptyString));
            return this;
        }

        public MapBuilder<T1, T2> setOption(TypeMappingOption option) {
            optionsList.add(option);
            return this;
        }

        public void map() {
            Mapper mapper = DozerBeanMapperBuilder.create()
                    .withMappingBuilder( new BeanMappingBuilder() {
                        protected void configure() {
                            mapping(
                                    t1.getClass(),
                                    t2.getClass(),
                                    optionsList.toArray(new TypeMappingOption[] {})
                            );
                        }
                    }).build();
            mapper.map(t1, t2);
        }
    }
}
