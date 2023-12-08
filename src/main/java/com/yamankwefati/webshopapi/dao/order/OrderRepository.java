package com.yamankwefati.webshopapi.dao.order;

import com.yamankwefati.webshopapi.model.ShopOrder;
import com.yamankwefati.webshopapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<ShopOrder, Long> {
    List<ShopOrder> getOrdersByUserId(User userId);
}
