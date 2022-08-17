package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteFromListServlet extends HttpServlet {

    private CartService cartService = DefaultCartService.getInstance();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long productId = Long.parseLong(req.getPathInfo().substring(1));
        cartService.delete(cartService.getCart(req), productId);
        resp.sendRedirect(req.getContextPath() + "/products/cart_list?delete=Product removed");
    }
}
