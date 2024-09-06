package com.example.shop.Sales;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.*;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    @Query(value = "SELECT o FROM Orders o JOIN FETCH o.member")
    List<Orders> customFindAll();
}
