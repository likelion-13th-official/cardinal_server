package com.likelionsg13th.cardinal.booth.repository.specification;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.common.enums.BoothCategory;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import org.springframework.data.jpa.domain.Specification;

public class BoothSpecification {

    /* 카테고리로 필터링*/
    public static Specification<Booth> hasCategory(BoothCategory category){
        return (root,query,criteriaBuilder)->
                criteriaBuilder.equal(root.get("category"),category);

    }

    /* 운영여부로 필터링*/
    public static Specification<Booth> isOperating(boolean isOperating){
        return (root,query,criteriaBuilder)->
                criteriaBuilder.equal(root.get("operatingInfo").get("isOperating"),isOperating);
    }

    /* 특정 요일 필터링*/
    public static Specification<Booth> hasDay(DayOfWeek day){
        return (root,query,criteriaBuilder)->
                criteriaBuilder.isMember(day,root.get("operatingDays"));
    }
}
