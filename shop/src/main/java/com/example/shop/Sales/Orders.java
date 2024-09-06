package com.example.shop.Sales;

import org.hibernate.annotations.CreationTimestamp;

import com.example.shop.User.UserInfo;

import java.time.LocalDateTime;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.ToString;
import jakarta.persistence.ForeignKey;

@Entity
@ToString
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private Integer price;
    private Integer count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserInfo member;

    @CreationTimestamp
    private LocalDateTime createdTime;

    public long getID() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getCount() {
        return count;
    }

    public UserInfo getMember() {
        return member;
    }

    public LocalDateTime getTime() {
        return createdTime;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setMember(UserInfo member) {
        this.member = member;
    }

    public void setTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
