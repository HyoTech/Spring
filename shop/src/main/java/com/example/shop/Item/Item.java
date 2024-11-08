package com.example.shop.Item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(indexes = @Index(name = "pName", columnList = "productName"))
@Getter
@Setter
public class Item {
    // @Id = Public key 설정, GenerateValue = auto increment 기능/ 컬럼 생성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 200)
    private String productName;
    private Integer price;
    private String writer;
    private String imgurl;

}
