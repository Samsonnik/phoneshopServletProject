package com.es.phoneshop.web;

import com.es.phoneshop.exception.PriceHistoryException;
import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.product.PriceHistory;
import com.es.phoneshop.product.Product;
import com.es.phoneshop.dao.ProductDao;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.math.BigDecimal;
import java.util.*;

public class DemoDataServletContextListener implements ServletContextListener {

    private ProductDao productDao;

    public DemoDataServletContextListener() {
        this.productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        boolean insertDemoData = Boolean.parseBoolean(event.getServletContext().getInitParameter("InsertDemoData"));
        if (insertDemoData) {
            try {
                for (Product product : saveSampleProducts()) {
                    productDao.save(product);
                }
            } catch (PriceHistoryException | ProductNotFoundException e) {
                throw new RuntimeException();
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private List<Product> saveSampleProducts() throws PriceHistoryException {
        List<Product> result = new ArrayList<>();
        Currency usd = Currency.getInstance("USD");

        List<PriceHistory> priceForSamsungGalaxyS = new ArrayList<>();
        priceForSamsungGalaxyS.add(new PriceHistory(PriceHistory.JANUARY_DATE(), new BigDecimal(90)));
        priceForSamsungGalaxyS.add(new PriceHistory(PriceHistory.MARCH_DATE(), new BigDecimal(96)));
        priceForSamsungGalaxyS.add(new PriceHistory(PriceHistory.JUNE_DATE(), new BigDecimal(100)));

        List<PriceHistory> priceForSamsungGalaxySIII = new ArrayList<>();
        priceForSamsungGalaxySIII.add(new PriceHistory(PriceHistory.JANUARY_DATE(), new BigDecimal(290)));
        priceForSamsungGalaxySIII.add(new PriceHistory(PriceHistory.MARCH_DATE(), new BigDecimal(296)));
        priceForSamsungGalaxySIII.add(new PriceHistory(PriceHistory.JUNE_DATE(), new BigDecimal(300)));

        List<PriceHistory> priceForAppleIphone6 = new ArrayList<>();
        priceForAppleIphone6.add(new PriceHistory(PriceHistory.JANUARY_DATE(), new BigDecimal(990)));
        priceForAppleIphone6.add(new PriceHistory(PriceHistory.MARCH_DATE(), new BigDecimal(996)));
        priceForAppleIphone6.add(new PriceHistory(PriceHistory.JUNE_DATE(), new BigDecimal(100)));


        result.add(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 15, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", priceForSamsungGalaxyS));
        result.add(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg", priceForSamsungGalaxySIII));
        result.add(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg", priceForAppleIphone6));
        result.add(new Product("iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        result.add(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        result.add(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        result.add(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        result.add(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        result.add(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        result.add(new Product("palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        result.add(new Product("simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        result.add(new Product("simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        result.add(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));
        return result;
    }
}
