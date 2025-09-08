package com.likelionsg13th.cardinal.common.service;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.domain.BoothDocument;
import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class IndexingService {

    private final BoothRepository boothRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @Transactional(readOnly = true)
    public void indexAllBooths() {
        log.info("Booth 데이터 전체 색인 시작");

        List<Booth> allBooths = boothRepository.findAll();

        //  BoothDocument로 변환
        List<BoothDocument> boothDocuments = allBooths.stream()
                .map(this::convertToDocument)
                .collect(Collectors.toList());

        if (boothDocuments.isEmpty()) {
            log.info("색인할 Booth 데이터가 없습니다.");
            return;
        }

        // ES에 Bulk 형태로 저장
        elasticsearchOperations.save(boothDocuments);

        log.info("총 {}개의 Booth 데이터 색인 완료", boothDocuments.size());
    }


    private BoothDocument convertToDocument(Booth booth) {
        BoothDocument doc = new BoothDocument();
        doc.setBoothId(booth.getId());
        doc.setName(booth.getName());
        doc.setCategory(booth.getCategory().name());


        List<BoothDocument.Menu> menuDocs = booth.getMenus().stream()
                .map(menuEntity -> {
                    BoothDocument.Menu menuDoc = new BoothDocument.Menu();
                    menuDoc.setItemName(menuEntity.getName());
                    menuDoc.setPrice(menuEntity.getPrice());
                    return menuDoc;
                })
                .collect(Collectors.toList());

        doc.setMenu(menuDocs);

        // id는 ES가 자동으로 생성하도록 null로 두거나, boothId와 동일하게 설정 가능
        // doc.setId(String.valueOf(booth.getId()));

        return doc;
    }
}