package com.example.shop.Infomation;

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
    public long Info_Id;

    public String Info_Title;
    public String Info_Value;
    public String Writer;
    public String Info_Date;
}
