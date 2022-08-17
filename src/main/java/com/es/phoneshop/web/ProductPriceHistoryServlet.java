package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.ProductNotFoundException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductPriceHistoryServlet extends HttpServlet {

    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getPathInfo().substring(1);
        Long finalProductId = Long.valueOf(productId);
        try {
            req.setAttribute("priceHistory", productDao.getProduct(finalProductId).getPriceHistory());
        } catch (ProductNotFoundException e) {
            req.setAttribute("error", "Product not found");
        }
        req.getRequestDispatcher("/WEB-INF/pages/productPriceHistory.jsp").forward(req, resp);
    }
}
