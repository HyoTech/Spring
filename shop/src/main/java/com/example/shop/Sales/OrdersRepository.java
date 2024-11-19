package com.example.shop.Sales;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import com.example.shop.User.UserInfo;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    @Query(value = "SELECT o FROM Orders o JOIN FETCH o.member")
    List<Orders> customFindAll();

    List<Orders> findByMember(UserInfo member);

    Orders findTop1ByMemberOrderByCreatedTimeDesc(UserInfo member);
}
