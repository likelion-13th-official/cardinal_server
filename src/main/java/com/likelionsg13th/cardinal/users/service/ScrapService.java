package com.likelionsg13th.cardinal.users.service;

import com.likelionsg13th.cardinal.common.enums.ContentType;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.users.exception.ScrapAlreadyExists;
import com.likelionsg13th.cardinal.users.exception.ScrapNotFoundException;
import com.likelionsg13th.cardinal.users.exception.UserNotFoundException;
import com.likelionsg13th.cardinal.common.provider.ProviderFactory;
import com.likelionsg13th.cardinal.users.domain.Scrap;
import com.likelionsg13th.cardinal.users.domain.Users;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import com.likelionsg13th.cardinal.users.dto.request.ScrapRequestDto;
import com.likelionsg13th.cardinal.users.repository.ScrapRepository;
import com.likelionsg13th.cardinal.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final ProviderFactory providerFactory;
    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;

    private final UserService userService;

    /*스크랩 : 굿즈 , 이벤트 , 부스 , 공연, */
    @Transactional
    public void addScrap(UserDto user, ScrapRequestDto scrapRequestDto) {
        Users  userEntity = checkUser(user);

        String content =  scrapRequestDto.getCategory();
        Long contentId = scrapRequestDto.getCategoryId();

        /*스크랩 가능한 카테고리인지 확인 */
        ContentType contentType = providerFactory.getProvider(content).ValidateContentExistsForScrap(contentId);

       /*이미 스크랩한 항목인 경우*/
        if(scrapRepository.existsByUser_IdAndContentIdAndContentType(user.getId(),contentId,contentType))
            throw new ScrapAlreadyExists(ErrorCode.SCRAP_ALREADY_EXISTS);

        Scrap scrap = Scrap.toEntity(contentType,contentId,userEntity);

        /*scrap 생성*/
        scrapRepository.save(scrap);
    }

    /*scrapId로 스크랩 해제 */
    public void deleteScrapByScrapId(UserDto userDto,Long scrapId) {
        Users user = checkUser(userDto);
        /*존재하지 않는 스크랩일 시 에러 반환*/
        if(!scrapRepository.existsByIdAndUserId(scrapId,user.getId()))
            throw new ScrapNotFoundException(ErrorCode.SCRAP_NOT_FOUND);

        scrapRepository.deleteById(scrapId);

    }

    @Transactional
    public void DeleteAllScrapByUserId(UserDto userDto) {
        Users user = checkUser(userDto);
        scrapRepository.deleteAllByUserId(user.getId());

    }

    /*user 검증 메서드 */
    private Users checkUser(UserDto user) {
        return userRepository.findById(user.getId()).orElseThrow(() ->  new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
