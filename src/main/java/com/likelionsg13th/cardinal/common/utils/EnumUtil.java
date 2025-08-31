package com.likelionsg13th.cardinal.common.utils;

import com.likelionsg13th.cardinal.common.enums.BoothCategory;
import com.likelionsg13th.cardinal.common.enums.ContentType;
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
        if(value==null || value.trim().isEmpty()){ throw new ParameterIsNullOrEmpty(ErrorCode.PARAMETER_IS_NULL_OR_EMPTY);}
        for(BoothCategory boothCategory : BoothCategory.values()){
            if(boothCategory.name().equalsIgnoreCase(value.trim())){return boothCategory;}
        }
        throw new InvalidCategoryException(ErrorCode.INVALID_CATEGORY);
    }
}
