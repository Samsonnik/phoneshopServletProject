package com.es.phoneshop.model.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.cart.Cart;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    Cart getCart(HttpServletRequest httpServletRequest);
    void add(Cart cart, Long productId, int quantity) throws ProductNotFoundException, OutOfStockException;
    void update(Cart cart, Long productId, int quantity) throws OutOfStockException;
    void delete(Cart cart, Long productId);
}
