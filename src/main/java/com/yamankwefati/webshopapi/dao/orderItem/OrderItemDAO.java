package com.yamankwefati.webshopapi.dao.orderItem;

import com.yamankwefati.webshopapi.dao.order.OrderRepository;
import com.yamankwefati.webshopapi.dao.product.ProductRepository;
import com.yamankwefati.webshopapi.model.OrderItem;
import com.yamankwefati.webshopapi.model.ShopOrder;
import javassist.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrderItemDAO {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderItemDAO(OrderItemRepository orderItemRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public List<OrderItem> getAllOrderItems() {
        return this.orderItemRepository.findAll();
    }

    public Optional<OrderItem> getOrderItemById(Long orderItemId) {
        return this.orderItemRepository.findById(orderItemId);
    }

    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        Optional<ShopOrder> shopOrder = this.orderRepository.findById(orderId);
        if (shopOrder.isEmpty()) {
            return List.of();
        }
        return this.orderItemRepository.getOrderItemsByShopOrderId(shopOrder.get());
    }

    public OrderItem saveNewOrderItem(OrderItem orderItem) throws NotFoundException {
        Optional<ShopOrder> shopOrder = this.orderRepository.findById(orderItem.getShopOrderId().getId());
        if (shopOrder.isEmpty()) {
            throw new NotFoundException("ShopOrder not found");
        }

        orderItem.setShopOrderId(shopOrder.get());

        // Ensure that the product with the given productId exists before saving
        if (!productRepository.existsById((long) orderItem.getProductId())) {
            throw new NotFoundException("Product not found");
        }

        return this.orderItemRepository.save(orderItem);
    }

    public OrderItem updateOrderItem(OrderItem newOrderItem, Long orderItemId) throws NotFoundException {
        Optional<OrderItem> oldOrderItem = this.orderItemRepository.findById(orderItemId);
        if (oldOrderItem.isEmpty()) {
            throw new NotFoundException("OrderItem with id: " + orderItemId + " not found");
        }

        OrderItem orderItem = oldOrderItem.get();
        orderItem.setProductId(newOrderItem.getProductId());
        orderItem.setQuantity(newOrderItem.getQuantity());
        orderItem.setSubtotal(newOrderItem.getSubtotal());

        return this.orderItemRepository.save(orderItem);
    }
}
