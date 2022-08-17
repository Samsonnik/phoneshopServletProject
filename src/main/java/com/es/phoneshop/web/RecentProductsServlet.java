package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.RecentProducts;
import com.es.phoneshop.product.Product;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class RecentProductsServlet extends HttpServlet {

    private List<Product> recentProductsList;

    @Override
    public void init() throws ServletException {
        super.init();
        recentProductsList = RecentProducts.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("recentProducts", recentProductsList);
        req.getRequestDispatcher("/WEB-INF/pages/recentProducts.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String remove = (req.getParameter("remove"));
        if (remove.equals("true")) {
            RecentProducts.removeList();
        }
        doGet(req, resp);
    }
}
