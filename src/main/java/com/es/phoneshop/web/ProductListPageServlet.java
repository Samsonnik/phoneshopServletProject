package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.dao.SortField;
import com.es.phoneshop.dao.SortOrder;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class ProductListPageServlet extends HttpServlet {

    private ProductDao productDao;
    private Cart cart;
    private CartService cartService;
    private Map<Long, String> errors;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");
        request.setAttribute("query", query);
        request.setAttribute("cartByIdAndQuantity", getCartByIdAndQuantity(request));
        request.setAttribute("products", productDao.findProducts(query,
                Optional.ofNullable(sortField).map(SortField::valueOf).orElse(null),
                Optional.ofNullable(sortOrder).map(SortOrder::valueOf).orElse(null)));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        errors = new HashMap<>();
        Long productId = Long.parseLong(req.getParameter("id"));
        req.setAttribute("errors", errors);
        try {
            cartService.add(cart, productId, 1);
            req.setAttribute("success", "Product added");
        } catch (ProductNotFoundException | OutOfStockException exception) {
            handleErrors(productId, exception);
        }
        doGet(req, resp);
    }

    private Map<Long, Integer> getCartByIdAndQuantity(HttpServletRequest request) {
        Map<Long, Integer> cartByIdAndQuantity = new TreeMap<>();
        cart = cartService.getCart(request);
        List<Long> productsId = cart.getCartItems().stream()
                .map(cartItem -> cartItem.getProduct().getId()).toList();
        List<Integer> quantityOfProducts = cart.getCartItems().stream()
                .map(CartItem::getQuantity).toList();
        productsId.forEach(productId ->
                cartByIdAndQuantity.put(productId, quantityOfProducts.get(productsId.indexOf(productId))));
        return cartByIdAndQuantity;
    }

    private void handleErrors(Long productId, Exception exception) {
        if (exception.getClass().equals(OutOfStockException.class)) {
            errors.put(productId, "Out of stock, available - " + ((OutOfStockException) exception).getStockAvailable()
                    + ", requested - " + ((OutOfStockException) exception).getStockRequested());
        }
        if (exception.getClass().equals(ProductNotFoundException.class)) {
            errors.put(productId, "Product not found");
        }
    }
}
