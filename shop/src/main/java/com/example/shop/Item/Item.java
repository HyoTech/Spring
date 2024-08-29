package com.example.shop.Item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
<<<<<<< HEAD
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(indexes = @Index(name = "pName", columnList = "productName"))
=======

@Entity
>>>>>>> c638f4723224459f81f45e8b8f8bb519460f7be8
public class Item {
    // @Id = Public key 설정, GenerateValue = auto increment 기능/ 컬럼 생성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;

    @Column(length = 200)
<<<<<<< HEAD
    private String productName;
    private Integer Price;
    private String Writer;
    private String imgurl;
=======
    private String ProductName;
    private Integer Price;
    private String Writer;
>>>>>>> c638f4723224459f81f45e8b8f8bb519460f7be8

    public long getID() {
        return ID;
    }

    public String getProductName() {
<<<<<<< HEAD
        return productName;
=======
        return ProductName;
>>>>>>> c638f4723224459f81f45e8b8f8bb519460f7be8
    }

    public Integer getPrice() {
        return Price;
    }

    public String getWriter() {
        return Writer;
    }

<<<<<<< HEAD
    public String imgurl() {
        return imgurl;
    }

    public void setProductName(String productName) {
        this.productName = productName;
=======
    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
>>>>>>> c638f4723224459f81f45e8b8f8bb519460f7be8
    }

    public void setPrice(Integer Price) {
        this.Price = Price;
    }

    public void setWriter(String Writer) {
        this.Writer = Writer;
    }
<<<<<<< HEAD

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
=======
    /*
     * public void setAge(Integer Age) {
     * if (Age <= 100 && 0 <= Age) {
     * this.Age = Age;
     * } else {
     * Age = Age;
     * }
     * }
     * 
     * public void PlusAge() {
     * this.Age = Age + 1;
     * }
     */
>>>>>>> c638f4723224459f81f45e8b8f8bb519460f7be8
}
