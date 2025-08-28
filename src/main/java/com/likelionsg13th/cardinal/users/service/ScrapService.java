package com.likelionsg13th.cardinal.users.service;

import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.ParameterIsNullOrEmpty;
import com.likelionsg13th.cardinal.common.exception.ScrapNotFoundException;
import com.likelionsg13th.cardinal.common.exception.UserNotFoundException;
import com.likelionsg13th.cardinal.common.provider.CategoryProvider;
import com.likelionsg13th.cardinal.common.provider.ProviderFactory;
import com.likelionsg13th.cardinal.users.domain.Scrap;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import com.likelionsg13th.cardinal.users.dto.requestDto.ScrapRequestDto;
import com.likelionsg13th.cardinal.users.repository.ScrapRepository;
import com.likelionsg13th.cardinal.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final ProviderFactory providerFactory;
    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;

    private final UserService userService;

    /*스크랩 : 굿즈 , 이벤트 , 부스 , 공연, */
    public void addScrap(UserDto user, ScrapRequestDto scrapRequestDto) {
        checkUser(user);

        String category =  scrapRequestDto.getCategory();
        Long categoryId = scrapRequestDto.getCategoryId();

        if(categoryId == null || category == null || category.isEmpty())
            throw new ParameterIsNullOrEmpty(ErrorCode.PARAMETER_IS_NULL_OR_EMPTY);

        CategoryProvider categoryProvider = providerFactory.getProvider(category);
        categoryProvider.addScrapByCategory(categoryId,user.getId());
    }

    /*scrapId로 스크랩 해제 */
    public void deleteScrapByScrapId(UserDto user,Long scrapId) {
        checkUser(user);
        /*존재하지 않는 스크랩일 시 에러 반환*/
        if(!scrapRepository.existsByIdAndUserId(scrapId,user.getId()))
            throw new ScrapNotFoundException(ErrorCode.SCRAP_NOT_FOUND);

        scrapRepository.deleteById(scrapId);

    }

    @Transactional
    public void DeleteAllScrapByUserId(UserDto user) {
        checkUser(user);
        scrapRepository.deleteAllByUserId(user.getId());

    }

    /*user 검증 메서드 */
    private void checkUser(UserDto user) {

        if(user == null || userRepository.findById(user.getId()).isEmpty())
            throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
    }
}
