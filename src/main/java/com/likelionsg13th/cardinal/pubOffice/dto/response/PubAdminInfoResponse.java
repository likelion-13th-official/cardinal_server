package com.likelionsg13th.cardinal.pubOffice.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PubAdminInfoResponse {
    private Long pubId;
    private String department;
}
