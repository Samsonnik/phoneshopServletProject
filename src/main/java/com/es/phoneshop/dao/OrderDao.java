package com.es.phoneshop.dao;

import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.product.Product;

public interface OrderDao {
    Order getOrder(String id) throws OrderNotFoundException;
    void save(Order order);
    void delete(String id) throws OrderNotFoundException;
}
