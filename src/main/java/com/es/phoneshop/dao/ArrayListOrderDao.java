package com.es.phoneshop.dao;

import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArrayListOrderDao implements OrderDao {

    private static OrderDao instance;
    private List<Order> ordersArrayList;

    private ArrayListOrderDao() {
        ordersArrayList = new ArrayList<>();
    }

    public static synchronized OrderDao getInstance(){
        if (instance == null){
            instance = new ArrayListOrderDao();
        }
        return instance;
    }

    @Override
    public Order getOrder(String id) throws OrderNotFoundException {
        return findOrder(id);
    }

    @Override
    public void save(Order order) {
        if (order != null){
            ordersArrayList.add(order);
        }
    }

    @Override
    public void delete(String id) throws OrderNotFoundException {
        Optional<Order> order = Optional.ofNullable(findOrder(id));
        order.ifPresent(value -> ordersArrayList.remove(value));
    }

    private Order findOrder(String id) throws OrderNotFoundException {
        return ordersArrayList.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst()
                .orElseThrow(OrderNotFoundException::new);
    }
}
