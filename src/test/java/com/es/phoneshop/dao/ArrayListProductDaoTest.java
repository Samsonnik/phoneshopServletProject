package com.es.phoneshop.dao;

import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.product.Product;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {

    private ProductDao productDao;
    private String query;
    private SortField sortField;
    private SortOrder sortOrder;
    private Currency usd = Currency.getInstance("USD");

    public ArrayListProductDaoTest() {
    }

    @Before
    public void setup() throws ProductNotFoundException {
        productDao = ArrayListProductDao.getInstance();
        productDao.save(new Product(999L, "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 15, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product(21L, "sgs", "Samsung Galaxy S III", new BigDecimal(100), usd, 15, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product(229L, "sgs", "Apple Iphone", new BigDecimal(100), usd, 15, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product(239L, "sgs", "Apple Iphone 2", new BigDecimal(100), usd, 15, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product("sgs", "Siemens c56", new BigDecimal(100), usd, 15, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
    }

    @Test
    public void testGetProduct() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product expectedProduct = new Product(999L, "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 15, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        assertNotNull(productDao.getProduct(999L));
        assertEquals(expectedProduct, productDao.getProduct(999L));
        try {
            productDao.getProduct(9999L);
            productDao.getProduct(123L);
            productDao.getProduct(4322L);
            Assert.fail("Expected ProductNotFoundException");
        } catch (ProductNotFoundException exception) {
            assertEquals(exception.getClass(), ProductNotFoundException.class);
        }
    }

    @Test
    public void testFindProducts(){
        List<Product> list = productDao.findProducts("Samsung", sortField, sortOrder);
        assertFalse(list.isEmpty());
        assertEquals(2, list.size());
        assertTrue(list.get(0).getDescription().contains("Samsung"));
        assertTrue(list.get(1).getDescription().contains("Samsung"));
        assertFalse(list.get(0).getDescription().contains("Iphone"));
        assertFalse(list.get(1).getDescription().contains("Siemens"));
    }

    @Test
    public void testSave() throws ProductNotFoundException {
        productDao.save(new Product(11L,"sgs", "TEST", new BigDecimal(100), usd, 15, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product("sgs", "TEST2", new BigDecimal(100), usd, -1, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product("sgs", "TEST3", new BigDecimal(-100), usd, -1, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));

        Optional<Product> product = productDao.findProducts("Test", sortField, sortOrder).stream()
                .filter(testProduct -> testProduct.getDescription().contains("TEST"))
                .findFirst();
        assertTrue(product.isPresent());

        Optional<Product> testNegativeStock = productDao.findProducts("Test", sortField, sortOrder).stream()
                .filter(testProduct -> testProduct.getDescription().contains("TEST2"))
                .findFirst();
        assertFalse(testNegativeStock.isPresent());

        Optional<Product> testIncorrectPrice = productDao.findProducts("Test", sortField, sortOrder).stream()
                .filter(testProduct -> testProduct.getDescription().contains("TEST3"))
                .findFirst();
        assertFalse(testIncorrectPrice.isPresent());

        assertEquals("TEST", productDao.getProduct(11L).getDescription());
        productDao.save(new Product(11L,"sgs", "UPDATE_TEST", new BigDecimal(100), usd, 15, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        assertEquals("UPDATE_TEST", productDao.getProduct(11L).getDescription());


    }

        @Test
        public void testDelete() {
            Optional<Product> product = productDao.findProducts("Siemens", sortField, sortOrder).stream()
                    .filter(someProduct -> someProduct.getDescription().contains("Siemens"))
                    .findFirst();
            assertTrue(product.isPresent());

            productDao.delete(product.get().getId());

            Optional<Product> product2 = productDao.findProducts("Siemens", sortField, sortOrder).stream()
                    .filter(someProduct -> Objects.equals(someProduct.getId(), product.get().getId()))
                    .findFirst();
            assertFalse(product2.isPresent());
        }

    @Test
    public void testNotNullProducts() {
        assertFalse(productDao.findProducts(query, sortField, sortOrder).isEmpty());
    }
}


