package com.es.phoneshop.model.cart;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCartServiceTest {

    private CartService cartService = DefaultCartService.getInstance();
    private ProductDao productDao = ArrayListProductDao.getInstance();

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Before
    public void setUp() throws Exception {
        when(request.getSession()).thenReturn(session);
        productDao.save(new Product(11L, "sgs", "TEST", new BigDecimal(100), Currency.getInstance("USD"),
                15, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product(12L, "sgs2", "TEST2", new BigDecimal(100), Currency.getInstance("USD"),
                15, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
    }

    @Test
    public void testGetCart() {
        Cart cart = cartService.getCart(request);
        assertNotNull(cart);
    }

    @Test
    public void testAdd() throws OutOfStockException, ProductNotFoundException {
        Cart cart = cartService.getCart(request);
        assertNotNull(cart);
        cartService.add(cart, 11L, 2);

        assertNotNull(cartService.getCart(request).getCartItems().stream()
                .filter(cartItem -> Objects.equals(cartItem.getProduct().getId(), 11L))
                .findFirst());
    }

    @Test
    public void testUpdate() throws OutOfStockException, ProductNotFoundException {
        Cart cart = cartService.getCart(request);
        cartService.add(cart, 11L, 2);
        ;
        assertNotNull(cart);
        assertEquals(2, cart.getTotalQuantity());
        assertNotEquals(3, cart.getTotalQuantity());
        cartService.update(cart, 11L, 3);
        assertEquals(3, cart.getTotalQuantity());
        assertNotEquals(2, cart.getTotalQuantity());
    }

    @Test
    public void testDelete() throws OutOfStockException, ProductNotFoundException {
        Cart cart = cartService.getCart(request);
        cartService.add(cart, 11L, 2);
        cartService.add(cart, 12L, 6);
        assertTrue(cart.getCartItems().stream()
                .anyMatch(cartItem -> cartItem.getProduct().getId().equals(12L)));
        cartService.delete(cart, 12L);
        assertFalse(cart.getCartItems().stream()
                .anyMatch(cartItem -> cartItem.getProduct().getId().equals(12L)));
    }
}
