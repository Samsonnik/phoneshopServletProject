package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.cart.RecentProducts;
import com.es.phoneshop.product.Product;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public class ProductDetailsPageServlet extends HttpServlet {

    private ProductDao productDao;
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Product product = productDao.getProduct(parseId(req));
            RecentProducts.addRecentProduct(product);
            req.setAttribute("product", product);
            req.setAttribute("cart", cartService.getCart(req));
        } catch (ProductNotFoundException e) {
            resp.setStatus(404);
        }
        req.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long productId = parseId(req);
        String stringQuantity = req.getParameter("quantity");
        Cart cart = DefaultCartService.getInstance().getCart(req);
        int quantity;
        try {
            NumberFormat numberFormat = NumberFormat.getInstance(req.getLocale());
            quantity = numberFormat.parse(stringQuantity).intValue();
            cartService.add(cart, productId, quantity);
            req.setAttribute("success", "Product added");
        } catch (ParseException exception) {
            req.setAttribute("error", "Incorrect input data");
            doGet(req, resp);
            return;
        } catch (OutOfStockException exception){
            req.setAttribute("outOfStock", "Out of stock, products available - " + exception.getStockAvailable());
            doGet(req, resp);
            return;
        } catch (ProductNotFoundException exception) {
            resp.setStatus(404);
        }
        resp.sendRedirect(req.getContextPath() + "/products/" + productId + "?success=Product added");
    }

    private Long parseId(HttpServletRequest request) {
        String productInfo = request.getPathInfo().substring(1);
        return Long.valueOf(productInfo);
    }
}
