package com.yamankwefati.webshopapi.dao.orderItem;

import com.yamankwefati.webshopapi.model.OrderItem;
import com.yamankwefati.webshopapi.model.ShopOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> getOrderItemsByShopOrderId(ShopOrder order);
}
