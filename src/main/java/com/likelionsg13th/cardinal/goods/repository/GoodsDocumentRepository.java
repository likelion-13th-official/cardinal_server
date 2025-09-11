package com.likelionsg13th.cardinal.goods.repository;

import com.likelionsg13th.cardinal.goods.domain.GoodsDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GoodsDocumentRepository extends ElasticsearchRepository<GoodsDocument,Integer> {
}
