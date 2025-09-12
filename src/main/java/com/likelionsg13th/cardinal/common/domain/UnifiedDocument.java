package com.likelionsg13th.cardinal.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnifiedDocument {

    @Field(name = "boothId", type = FieldType.Keyword)
    private Long boothId;

    @Field(name = "eventId", type = FieldType.Keyword)
    private Long eventId;

    @Field(name = "goodsId", type = FieldType.Keyword)
    private Long goodsId;

    public Long getEntityId() {
        if (boothId != null) return boothId;
        if (eventId != null) return eventId;
        return goodsId;
    }
}