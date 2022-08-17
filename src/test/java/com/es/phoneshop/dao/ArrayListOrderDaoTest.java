package com.es.phoneshop.dao;

import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;

public class ArrayListOrderDaoTest {

    private OrderDao orderDao;
    private String id = UUID.randomUUID().toString();
    private String NOT_FOUND = "Order not found";

    @Before
    public void setup() {
        orderDao = ArrayListOrderDao.getInstance();
        Order order = new Order(id, new BigDecimal(13), new BigDecimal(12),
                "Vova", "Vovka", "+375333147704", new Date().toString(),
                "Osp", PaymentMethod.CACHE);
        orderDao.save(order);
    }

    @Test
    public void getOrder() {
        try {
            Order order = orderDao.getOrder(id);
            assertNotNull(order);
        } catch (OrderNotFoundException exception){
            Assert.fail(NOT_FOUND);
        }
    }

    @Test
    public void saveOrder() {
        Order order = new Order("222", new BigDecimal(13), new BigDecimal(12),
                "vovkaTest", "Vovka", "+375333147704", new Date().toString(),
                "Osp", PaymentMethod.CACHE);
        orderDao.save(order);
        try {
            assertEquals(orderDao.getOrder("222").getFirstName(), "vovkaTest");
        } catch (OrderNotFoundException exception) {
            Assert.fail(NOT_FOUND);
        }
    }

    @Test
    public void deleteOrder() {
        Order order = new Order("123", new BigDecimal(13), new BigDecimal(12),
                "vovkaTest", "Vovka", "+375333147704", new Date().toString(),
                "Osp", PaymentMethod.CACHE);
        orderDao.save(order);
        try {
            assertEquals(orderDao.getOrder("123").getFirstName(), "vovkaTest");
            orderDao.delete("123");
            try {
                orderDao.getOrder("123");
                Assert.fail("There are must be an exception");
            } catch (OrderNotFoundException exception) {
                assertSame(exception.getClass(), OrderNotFoundException.class);
            }
        } catch (OrderNotFoundException exception) {
            Assert.fail(NOT_FOUND);
        }
    }
}
