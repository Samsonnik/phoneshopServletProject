package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.order.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class CheckoutPageServlet extends HttpServlet {

    private OrderService orderService;
    private CartService cartService;
    private Map<String, String> errors;

    public CheckoutPageServlet() {
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cart cart = cartService.getCart(req);
        Order order = orderService.getOrder(cart);
        req.setAttribute("order", order);
        req.setAttribute("items", order.getCartItems());
        req.setAttribute("paymentMethods", orderService.getPaymentMethods());
        req.getRequestDispatcher("/WEB-INF/pages/checkoutPage.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        errors = new HashMap<>();
        Order order = OrderCheckerAndSetter.checkAndSetValues(req, orderService, cartService, errors);
        if (errors.isEmpty()) {
            orderService.saveOrder(order);
            resp.sendRedirect(req.getContextPath() + "/order/overview/" + order.getId());
        } else {
            req.setAttribute("errors", errors);
            req.setAttribute("order", order);
            req.setAttribute("paymentMethods", orderService.getPaymentMethods());
            req.getRequestDispatcher("/WEB-INF/pages/checkoutPage.jsp").forward(req, resp);
        }
    }
}
