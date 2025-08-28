package com.likelionsg13th.cardinal.common.provider;

import com.likelionsg13th.cardinal.booth.exception.BoothNotFoundException;
import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.ScrapAlreadyExists;
import com.likelionsg13th.cardinal.common.exception.UserNotFoundException;
import com.likelionsg13th.cardinal.users.domain.Scrap;
import com.likelionsg13th.cardinal.users.domain.Users;
import com.likelionsg13th.cardinal.users.repository.ScrapRepository;
import com.likelionsg13th.cardinal.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.likelionsg13th.cardinal.common.enums.ContentType.BOOTH;

@Component
@RequiredArgsConstructor
public class BoothProvider implements CategoryProvider {
    private final BoothRepository boothRepository;
    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;

    @Override
    public boolean hasCategory(String category){
        return BOOTH.name().equalsIgnoreCase(category);
    }

    @Override
    public Object getMapMarkersByCategory() {
        return null;
    }

    @Override
    public void addScrapByCategory(Long categoryId, Long userId) {
        if(!boothRepository.existsById(categoryId))
            throw new BoothNotFoundException(ErrorCode.BOOTH_NOT_FOUND);

        Users userEntity = userRepository.findById(userId).orElseThrow(
              ()-> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)
        );

        if(scrapRepository.existsByUser_IdAndContentIdAndContentType(userId,categoryId,BOOTH))
            throw new ScrapAlreadyExists(ErrorCode.SCRAP_ALREADY_EXISTS);

        Scrap scrap = Scrap.toEntity(BOOTH,categoryId,userEntity);

        //scrap 저장
        scrapRepository.save(scrap);
    }



//    @Override
//    public Object getMapMarkersByCategory(String category) {
//        BoothCategory boothCategory= boothCategoryValueOfIgnoreCase(category);
//
//        MapInfoDto mapInfo = MapInfoDto.from(BoothRepository.findLocationFirstByIdAndCategory(boothCategory));
//
//        return MapFilteredByCategoryDto.from(List.of(mapInfo),boothCategory.name());
//
//    }


//

}
