package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class CartListServlet extends HttpServlet {

    private CartService defaultCartService;
    private Cart cart;
    private Map<Long, String> errors;
    private Long productId;

    @Override
    public void init() {
        defaultCartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        cart = defaultCartService.getCart(req);
        req.setAttribute("cart", cart.getCartItems());
        req.setAttribute("totalPrice", cart.getTotalPrice());
        req.getRequestDispatcher("/WEB-INF/pages/cartList.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        errors = new HashMap<>();
        updateData(req);
        if (errors.size() == 0) {
            resp.sendRedirect(req.getContextPath() + "/products/cart_list" + "?success=Products added");
        } else {
            req.setAttribute("errors", errors);
            doGet(req, resp);
        }
    }

    private void handleErrors(Long productId, Exception exception) {
        if (exception.getClass().equals(NumberFormatException.class)) {
            errors.put(productId, "Not a number");
        }
        if (exception.getClass().equals(OutOfStockException.class)) {
            errors.put(productId, "Out of stock" + ": " + "available - " + ((OutOfStockException) exception).getStockAvailable()
                    + ", " + "requested - " + ((OutOfStockException) exception).getStockRequested());
        }
    }

    private void updateData(HttpServletRequest request) {
        List<String> quantityParamList = Arrays.asList(request.getParameterValues("quantity"));
        List<String> idParamList = Arrays.asList(request.getParameterValues("id"));
        quantityParamList.forEach(quantityString -> {
            try {
                productId = (long) parseString(idParamList.get(quantityParamList.indexOf(quantityString)));
                int quantity = parseString(quantityString);
                defaultCartService.update(cart, productId, quantity);
            } catch (NumberFormatException | OutOfStockException exception) {
                handleErrors(productId, exception);
            }
        });
    }

    private int parseString(String stringForParsing) {
        return Integer.parseInt(stringForParsing);
    }
}


