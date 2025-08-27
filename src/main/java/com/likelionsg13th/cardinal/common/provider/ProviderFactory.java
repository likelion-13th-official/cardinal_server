package com.likelionsg13th.cardinal.common.provider;

import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.InvalidParameterException;
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

    //find a provider
    public CategoryProvider getProvider(String category) {
        return categoryProviders.stream()
                .filter(provider -> provider.hasCategory(category))
                .findFirst()
                .orElseThrow(() -> new InvalidParameterException(ErrorCode.INVALID_CATEGORY));
    }
}
