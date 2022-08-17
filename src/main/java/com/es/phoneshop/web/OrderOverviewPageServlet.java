package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListOrderDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderOverviewPageServlet extends HttpServlet {

    private OrderDao orderDao;
    private Order order;

    @Override
    public void init() throws ServletException {
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderId = req.getPathInfo().substring(1);
        try {
            order = orderDao.getOrder(orderId);
            req.setAttribute("items", order.getCartItems());
            req.setAttribute("order", order);
            req.getRequestDispatcher("/WEB-INF/pages/orderOverviewPage.jsp").forward(req, resp);
        } catch (OrderNotFoundException e) {
            req.setAttribute("error", "Order not found");
        }
    }
}
