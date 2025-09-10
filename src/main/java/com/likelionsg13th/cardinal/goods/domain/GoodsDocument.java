package com.likelionsg13th.cardinal.goods.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@Setter
@Document(indexName = "goods")
@Setting(settingPath = "elasticsearch/analyzer-settings.json") // Assuming a shared setting file
public class GoodsDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private Long goodsId;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "nori"),
            otherFields = {
                    @InnerField(suffix = "autocomplete", type = FieldType.Search_As_You_Type, analyzer = "nori")
            }
    )
    private String name;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String description;

    @Field(type = FieldType.Text, analyzer = "contents_type_analyzer")
    private String type;


}