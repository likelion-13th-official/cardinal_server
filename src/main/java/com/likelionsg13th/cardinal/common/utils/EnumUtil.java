package com.likelionsg13th.cardinal.common.utils;

import com.likelionsg13th.cardinal.common.enums.BoothCategory;
import com.likelionsg13th.cardinal.common.enums.ContentType;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.InvalidCategoryException;
import com.likelionsg13th.cardinal.common.exception.ParameterIsNullOrEmpty;

import javax.swing.text.AbstractDocument;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class EnumUtil {

    //ContentType + BoothCategory
    public static List<String> getCombinedCategoriesName(){
        Stream<String> contentTypeStream = Arrays.stream(ContentType.values()).map(Enum::name);
        Stream<String> boothCategoryStream = Arrays.stream(BoothCategory.values()).map(Enum::name);

        return Stream.concat(contentTypeStream,boothCategoryStream).toList();
    }

    //String -> BoothCategory
    public static BoothCategory boothCategoryValueOfIgnoreCase(String value){

        for(BoothCategory boothCategory : BoothCategory.values()){
            if(boothCategory.name().equalsIgnoreCase(value.trim())){return boothCategory;}
        }
        throw new InvalidCategoryException(ErrorCode.INVALID_CATEGORY);
    }

    public static DayOfWeek DayOfWeekValueOfIgnoreCase(String value){

        for(DayOfWeek day : DayOfWeek.values()){
            if(day.name().equalsIgnoreCase(value.trim())){return day;}
        }

       // TODO : 예외 처리 수정
        throw new InvalidCategoryException(ErrorCode.INVALID_CATEGORY);
    }
}
