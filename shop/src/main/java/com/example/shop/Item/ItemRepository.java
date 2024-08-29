package com.example.shop.Item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findPageBy(Pageable page);

    @Query(value = "SELECT * FROM shop.item WHERE MATCH(product_name) AGAINST(?1)", nativeQuery = true)
    Page<Item> fullTextSearch(String text, Pageable page);
}
