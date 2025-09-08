package com.likelionsg13th.cardinal.booth.repository;

import com.likelionsg13th.cardinal.booth.domain.BoothDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BoothDocumentRepository extends ElasticsearchRepository<BoothDocument,String> {
}
