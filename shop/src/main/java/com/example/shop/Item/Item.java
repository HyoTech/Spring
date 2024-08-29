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
    private long ID;

    @Column(length = 200)
    private String productName;
    private Integer Price;
    private String Writer;
    private String imgurl;

    public long getID() {
        return ID;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getPrice() {
        return Price;
    }

    public String getWriter() {
        return Writer;
    }

    public String imgurl() {
        return imgurl;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(Integer Price) {
        this.Price = Price;
    }

    public void setWriter(String Writer) {
        this.Writer = Writer;
    }

    public void setimgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + ID +
                ", productName='" + productName + '\'' +
                ", price=" + Price +
                ", imgurl=" + imgurl +
                '}';
    }
}
