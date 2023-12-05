package com.yamankwefati.webshopapi.controller;

import com.yamankwefati.webshopapi.dao.order.OrderDAO;
import com.yamankwefati.webshopapi.model.ApiResponse;
import com.yamankwefati.webshopapi.model.ShopOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderDAO orderDAO;

    public OrderController(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    //Get all orders
    @RequestMapping(method = RequestMethod.GET, value = "/all-orders")
    @ResponseBody
    ApiResponse<List<ShopOrder>> getAllOrders(){
        try {
            List<ShopOrder> shopOrders = this.orderDAO.getAllOrders();
            return new ApiResponse<>(HttpStatus.ACCEPTED, shopOrders);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, "Could not fetch all orders");
        }
    }

    //Get one order by id
    @RequestMapping(method = RequestMethod.GET, value = "/{orderId}")
    @ResponseBody
    ApiResponse<Optional<ShopOrder>> getOrderBy(@PathVariable Long orderId) {
        try {
            Optional<ShopOrder> order = this.orderDAO.getOrderById(orderId);
            return new ApiResponse<>(HttpStatus.ACCEPTED, order);
        } catch (Exception e){
            return new ApiResponse<>(HttpStatus.NOT_FOUND, "Order not found");
        }
    }

    //Place a new order
    @RequestMapping(method = RequestMethod.POST, value = "/new-order")
    @ResponseBody
    public ApiResponse<ShopOrder> placeNewOrder(
            @RequestBody ShopOrder shopOrderRequest
    ){
        try {
            ShopOrder order = this.orderDAO.saveNewOrder(shopOrderRequest);
            return new ApiResponse<>(HttpStatus.ACCEPTED, order);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, "Couldn't place order.");
        }
    }

}
