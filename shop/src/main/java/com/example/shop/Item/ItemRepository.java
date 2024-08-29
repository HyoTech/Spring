package com.example.shop.Item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findPageBy(Pageable page);

    // 인덱스를 이용한 쿼리 검색
    List<Item> findAllByProductNameContains(String productName);

    // 풀텍스트 인덱스(ngram 파서)를 이용한 쿼리 검색
    @Query(value = "SELECT * FROM shop.item WHERE MATCH(product_name) AGAINST(?1)", nativeQuery = true)
    Page<Item> fullTextSearch(String text, Pageable page);
}
