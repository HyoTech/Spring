package com.example.shop.Item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(indexes = @Index(name = "pName", columnList = "productName"))
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

    public long getID() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getPrice() {
        return price;
    }

    public String getWriter() {
        return writer;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
