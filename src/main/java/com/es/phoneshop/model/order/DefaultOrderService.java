package com.es.phoneshop.model.order;

import com.es.phoneshop.dao.ArrayListOrderDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


public class DefaultOrderService implements OrderService {

    private static OrderService instance;
    private Order order;
    private CartItem cartItem;
    private OrderDao orderDao;

    private DefaultOrderService() {
    }

    public static synchronized OrderService getInstance(){
        if (instance == null){
            instance = new DefaultOrderService();
        }
        return instance;
    }

    @Override
    public Order getOrder(Cart cart) {
        order = new Order();
        order.setCartItems(cart.getCartItems().stream()
                .map(item -> {
            try {
                cartItem = (CartItem) item.clone();
            } catch (CloneNotSupportedException e) {
               throw new RuntimeException();
            }
            return cartItem;
        }).collect(Collectors.toList()));
        order.setOrderDate(getDate());
        order.setSubtotal(cart.getTotalPrice());
        order.setDeliveryCost(calculateDeliveryCost());
        order.setTotalPrice(order.getSubtotal().add(order.getDeliveryCost()));
        return order;
    }

    private String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void setId(Order order){
        order.setId(UUID.randomUUID().toString());
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return List.of(PaymentMethod.values());
    }

    @Override
    public void saveOrder(Order order) {
        if (order == null) return;
        setId(order);
        orderDao = ArrayListOrderDao.getInstance();
        orderDao.save(order);
    }

    @Override
    public void deleteOrder(Order order) throws OrderNotFoundException {
        orderDao = ArrayListOrderDao.getInstance();
        orderDao.delete(order.getId());
    }

    private BigDecimal calculateDeliveryCost(){
        return new BigDecimal(5);
    }
}
