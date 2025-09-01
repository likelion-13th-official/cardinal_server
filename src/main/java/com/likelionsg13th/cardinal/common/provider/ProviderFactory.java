package com.likelionsg13th.cardinal.common.provider;

import com.likelionsg13th.cardinal.common.enums.ContentType;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.InvalidCategoryException;
import com.likelionsg13th.cardinal.users.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

/*
* Provider 찾아주는 클래스 .
* TODO : 예외처리 수정
* */
@Component
@RequiredArgsConstructor
public class ProviderFactory {

    //all  provider DI
    private final List<CategoryProvider> categoryProviders;
    private final List<Scrappable> scrappables;


    //find a provider
    public CategoryProvider getProvider(String Category) {

        return categoryProviders.stream()
                .filter(provider -> provider.hasCategory(Category.trim()))
                .findFirst()
                .orElseThrow(() -> new InvalidCategoryException(ErrorCode.INVALID_CATEGORY));
    }

    public Scrappable getScrappable(String Category) {
        return scrappables.stream()
                .filter(scrappable -> scrappable.hasCategory(Category.trim()))
                .findFirst()
                .orElseThrow(()->  new InvalidCategoryException(ErrorCode.SCRAP_NOT_SUPPORTED_FOR_CATEGORY));

    }
}
