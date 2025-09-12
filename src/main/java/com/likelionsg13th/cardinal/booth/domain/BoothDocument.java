package com.likelionsg13th.cardinal.booth.domain;

import com.likelionsg13th.cardinal.common.domain.OperatingInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalTime;
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


    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "nori"),
            otherFields = {
                    @InnerField(suffix = "autocomplete", type = FieldType.Search_As_You_Type, analyzer = "nori")
            }
    )
    private String name;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String description;


    //부스 하위 타입 5개
    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "booth_category_analyzer"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword)
            }
    )
    private String category;

    //부스
    @Field(type = FieldType.Text, analyzer = "contents_type_analyzer")
    private String type;


    @Field(type = FieldType.Text, analyzer = "nori")
    private List<String> menu;

    @Field(type = FieldType.Text, fielddata = true)
    private String location;

    // OperatingInfo를 개별 필드로 분리
    @Field(type = FieldType.Date, format = DateFormat.hour_minute_second)
    private LocalTime startTime;

    @Field(type = FieldType.Date, format = DateFormat.hour_minute_second)
    private LocalTime endTime;

    @Field(type = FieldType.Keyword)
    private List<String> operatingDays;

    @Field(type = FieldType.Text)
    private String thumbnailUrl;


}