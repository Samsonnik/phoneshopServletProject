package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static  org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultOrderServiceTest {

    private OrderService orderService = DefaultOrderService.getInstance();

    @Mock
    private Cart cart;

    @Before
    public void setUp() throws Exception {
        Product product = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), Currency.getInstance("USD"),
                15, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product product1 = new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), Currency.getInstance("USD"),
                5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg");
        List<CartItem> list = new ArrayList<>();
        list.add(new CartItem(product, 2));
        list.add(new CartItem(product1, 1));
        cart = new Cart(list, 3, BigDecimal.valueOf(200));
    }

    @Test
    public void testGetOrder() {
        Order order = orderService.getOrder(cart);
        assertNotNull(order);
    }

    @Test
    public void testGetPaymentMethods() {
        PaymentMethod cache = PaymentMethod.CACHE;
        PaymentMethod creditCart = PaymentMethod.CREDIT_CART;
        List<PaymentMethod> list = orderService.getPaymentMethods();
        assertNotNull(list);
        list.forEach(value -> {
            assertTrue(value.equals(cache) || value.equals(creditCart));
        });
    }
}
