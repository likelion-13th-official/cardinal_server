package com.likelionsg13th.cardinal.event.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.List;

@Getter
@Setter
@Document(indexName = "events")
@Setting(settingPath = "/elasticsearch/analyzer-settings.json")
public class EventDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private Long eventId;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "nori"),
            otherFields = {
                    @InnerField(suffix = "autocomplete", type = FieldType.Search_As_You_Type, analyzer = "nori")
            }
    )
    private String name;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String description;

    @Field(type = FieldType.Text, fielddata = true)
    private String location;

    // OperatingInfo를 개별 필드로 분리
    @Field(type = FieldType.Text)
    private String startTime;

    @Field(type = FieldType.Text)
    private String endTime;

    @Field(type = FieldType.Keyword)
    private List<String> operatingDays;

    @Field(type = FieldType.Text)
    private String thumbnailUrl;


    @Field(type = FieldType.Text, analyzer = "contents_type_analyzer")
    private String type;


}