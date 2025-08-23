package com.likelionsg13th.cardinal.common.utils;

import com.likelionsg13th.cardinal.common.enums.BoothCategory;
import com.likelionsg13th.cardinal.common.enums.ContentType;

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
}
