package ru.isands.test.estore.configuration;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

public class StrictPageableResolver extends PageableHandlerMethodArgumentResolver {
    public StrictPageableResolver() {
        super();
    }

    /**
     * Метод вызывается при создании экземпляра Pageable в контроллере, изменена логика проверки
     * параметров page и size - неправильные значения не заменяются значениями по умолчанию,
     * а провоцируют {@link IllegalArgumentException}
     */
    @NonNull
    @Override
    public Pageable resolveArgument(@NonNull MethodParameter methodParameter,
                                    ModelAndViewContainer mavContainer,
                                    @NonNull NativeWebRequest webRequest,
                                    WebDataBinderFactory binderFactory) {

        Pageable defaultPageable = super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);

        String pageParam = webRequest.getParameter(getPageParameterName());
        String sizeParam = webRequest.getParameter(getSizeParameterName());
        int page = defaultPageable.getPageNumber();
        int size = defaultPageable.getPageSize();

        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Invalid page value: " + pageParam, ex);
            }
        }
        if (sizeParam != null) {
            try {
                size = Integer.parseInt(sizeParam);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Invalid size value: " + sizeParam, ex);
            }
        }
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be less than zero: " + page);
        }
        if (size < 1) {
            throw new IllegalArgumentException("Page size must be at least one: " + size);
        }
        Sort sort = defaultPageable.getSort();

        return PageRequest.of(page, size, sort);
    }
}

