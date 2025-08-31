package com.likelionsg13th.cardinal.users.service;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.common.dto.resonseDto.ListResponseDto;
import com.likelionsg13th.cardinal.common.enums.ContentType;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.ScrapAlreadyExists;
import com.likelionsg13th.cardinal.common.exception.ScrapNotFoundException;
import com.likelionsg13th.cardinal.common.exception.UserNotFoundException;
import com.likelionsg13th.cardinal.common.provider.ProviderFactory;
import com.likelionsg13th.cardinal.users.domain.Scrap;
import com.likelionsg13th.cardinal.users.domain.Users;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import com.likelionsg13th.cardinal.users.dto.requestDto.ScrapRequestDto;
import com.likelionsg13th.cardinal.users.dto.resonseDto.ScrapCommonDto;
import com.likelionsg13th.cardinal.users.repository.ScrapRepository;
import com.likelionsg13th.cardinal.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.likelionsg13th.cardinal.common.enums.ContentType.BOOTH;
import static com.likelionsg13th.cardinal.common.enums.ContentType.GOODS;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final ProviderFactory providerFactory;
    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;

    /*
    *   운영 여부: 운영 중 / 운영 종료
        요일: 월 / 화 / 수 / 목 / 금 : 상시 일경우 모든 경우에 반환
        * *
* dto : 공통(카테고리,subCategory,썸네일,매장 이름,장소,요일)
- 부스 : + 시작시간,끝시간,
- 이벤트 : + 시작시간,끝시간,
- 공연
    - 아티스트, 동아리 : X
    - 영화제 :  +시작시간,끝시간
- 굿즈 : +가격
*
* category : booth
* subCategory : 포토부스,주점,어쩌고 등등
* */

    public ListResponseDto<ScrapCommonDto> getAllScrapsByDayOrIsOperation(UserDto user, String day, Boolean isOperating){
        Users  userEntity = checkUser(user);

        List<Scrap> scrapList = scrapRepository.findAllByUser_Id(userEntity.getId());

        List<ScrapCommonDto>  scrapCommonDtoList =  scrapList.stream()
                .map(
                        scrap -> {
                            if(scrap.getContentType().equals(GOODS)){ //GOODS는 filter 없는 버전사용.
                                return  providerFactory.getScrappable(scrap.getContentType().toString())
                                        .getScrapCommonDto(scrap.getContentId());
                            }
                            return
                                    providerFactory.getScrappable(scrap.getContentType().toString())
                                    .getScrapCommonDto(scrap.getContentId(), day, isOperating);

                        }
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        return ListResponseDto.from(scrapCommonDtoList);
    }


    /*
     * 유저와 부스 리스트를 비교하여 스크랩 한 부스를 찾습니다.
     * @param boothList 스크랩 여부 확인할 부스 리스트
     * @param user 로그인한 사용자 정보 null 인 경우 빈 set 반환
     */
    public Set<Long> getBookMarkedBoothIds(List<Booth> boothList, UserDto user){
        if(user == null ) return Collections.emptySet();
        List<Long> boothIds = boothList.stream().map(Booth::getId).toList();
        return scrapRepository.findAllByUser_IdAndContentIdInAndContentType(user.getId(),boothIds,BOOTH)
                .stream()
                .map(Scrap::getContentId)
                .collect(Collectors.toSet());
    }


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
