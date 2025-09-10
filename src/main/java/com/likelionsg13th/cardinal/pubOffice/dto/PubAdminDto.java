package com.likelionsg13th.cardinal.pubOffice.dto;

import com.likelionsg13th.cardinal.pubOffice.domain.PubAdmin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PubAdminDto {
    private Long id;
    private String department;
    private String adminId;
    private Long pubId;

    public static PubAdminDto from(PubAdmin pubAdmin){
        return PubAdminDto.builder()
                .id(pubAdmin.getId())
                .adminId(pubAdmin.getAdminId())
                .department(pubAdmin.getDepartment())
                .pubId(pubAdmin.getBooth().getId())
                .build();
    }
}
