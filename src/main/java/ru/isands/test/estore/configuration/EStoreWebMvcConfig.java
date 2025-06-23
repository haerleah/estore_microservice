package ru.isands.test.estore.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class EStoreWebMvcConfig implements WebMvcConfigurer {
    /**
     * Заменяем стандартный {@link PageableHandlerMethodArgumentResolver} на
     * кастомный {@link StrictPageableResolver}, что убирает подавление некоррктных значений.
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new StrictPageableResolver());
    }
}
