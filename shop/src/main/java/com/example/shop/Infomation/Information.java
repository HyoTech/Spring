package com.example.shop.Infomation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Information {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "info_id")
    public long id;

    public String Info_Title;
    public String Info_Value;

    @Column(name = "writer")
    public String Writer;
    public String Info_Date;
    public Integer View;
}
