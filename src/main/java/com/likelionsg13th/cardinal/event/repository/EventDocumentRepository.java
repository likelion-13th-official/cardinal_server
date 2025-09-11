package com.likelionsg13th.cardinal.event.repository;

import com.likelionsg13th.cardinal.event.domain.EventDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EventDocumentRepository extends ElasticsearchRepository<EventDocument, String> {
}
