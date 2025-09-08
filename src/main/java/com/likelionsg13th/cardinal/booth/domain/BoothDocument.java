package com.likelionsg13th.cardinal.booth.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.List;

@Getter
@Setter
@Setting(settingPath = "/elasticsearch/analyzer-settings.json")
@Document(indexName = "booths")
public class BoothDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private Long boothId;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String name;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String description;


    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "booth_category_analyzer"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword)
            }
    )
    private String category;

    @Field(type = FieldType.Nested)
    private List<Menu> menu;

    @Getter
    @Setter
    public static class Menu {
        @Field(type = FieldType.Text, analyzer = "nori")
        private String itemName;

        @Field(type = FieldType.Integer)
        private Integer price;
    }
}