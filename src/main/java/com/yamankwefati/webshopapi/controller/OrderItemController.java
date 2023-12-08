package com.yamankwefati.webshopapi.controller;

import com.yamankwefati.webshopapi.dao.orderItem.OrderItemDAO;
import com.yamankwefati.webshopapi.model.ApiResponse;
import com.yamankwefati.webshopapi.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/order-items")
public class OrderItemController {

    @Autowired
    private OrderItemDAO orderItemDAO;

    public OrderItemController(OrderItemDAO orderItemDAO) {
        this.orderItemDAO = orderItemDAO;
    }

    // Get all order items
    @GetMapping("/all")
    @ResponseBody
    public ApiResponse<List<OrderItem>> getAllOrderItems() {
        try {
            List<OrderItem> orderItems = orderItemDAO.getAllOrderItems();
            return new ApiResponse<>(HttpStatus.ACCEPTED, orderItems);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, "Could not fetch all order items");
        }
    }

    // Get one order item by id
    @GetMapping("/{orderItemId}")
    @ResponseBody
    public ApiResponse<Optional<OrderItem>> getOrderItemById(@PathVariable Long orderItemId) {
        try {
            Optional<OrderItem> orderItem = orderItemDAO.getOrderItemById(orderItemId);
            return new ApiResponse<>(HttpStatus.ACCEPTED, orderItem);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, "Order item not found");
        }
    }

    // Get order items by order id
    @GetMapping("/by-order/{orderId}")
    @ResponseBody
    public ApiResponse<List<OrderItem>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        try {
            List<OrderItem> orderItems = orderItemDAO.getOrderItemsByOrderId(orderId);
            return new ApiResponse<>(HttpStatus.ACCEPTED, orderItems);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, "Order items not found for the given order ID");
        }
    }

    // Place a new order item
    @PostMapping("/new-order-item")
    @ResponseBody
    public ApiResponse<OrderItem> placeNewOrderItem(@RequestBody OrderItem orderItemRequest) {
        try {
            OrderItem orderItem = orderItemDAO.saveNewOrderItem(orderItemRequest);
            return new ApiResponse<>(HttpStatus.ACCEPTED, orderItem);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, "Couldn't place order item.");
        }
    }

    // Update an order item
    @PutMapping("/{orderItemId}")
    @ResponseBody
    public ApiResponse<OrderItem> updateOrderItem(
            @RequestBody OrderItem orderItemRequest,
            @PathVariable Long orderItemId) {
        try {
            OrderItem orderItem = orderItemDAO.updateOrderItem(orderItemRequest, orderItemId);
            return new ApiResponse<>(HttpStatus.ACCEPTED, orderItem);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, "Couldn't update order item.");
        }
    }
}
