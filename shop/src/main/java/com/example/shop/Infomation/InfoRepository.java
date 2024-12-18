package com.example.shop.Infomation;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import lombok.NonNull;

public interface InfoRepository extends JpaRepository<Information, Long> {
    List<Information> findAll(@NonNull Sort sort);

    Page<Information> findPageBy(Pageable page);
}
