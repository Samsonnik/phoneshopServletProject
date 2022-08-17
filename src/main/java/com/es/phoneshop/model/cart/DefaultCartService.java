package com.es.phoneshop.model.cart;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultCartService implements CartService {

    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";
    private static CartService instance;
    private ProductDao productDao;

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static synchronized CartService getInstance() {
        if (instance == null) {
            instance = new DefaultCartService();
        }
        return instance;
    }

    @Override
    public synchronized Cart getCart(HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
        }
        return cart;
    }

    @Override
    public synchronized void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        try {
            Product product = productDao.getProduct(productId);
            if (quantity > product.getStock()) {
                throw new OutOfStockException(product, product.getStock(), quantity);
            }
            Optional<CartItem> cartItem = cart.getCartItems().stream()
                    .filter(itemProduct -> itemProduct.getProduct().getCode().equals(product.getCode()))
                    .findFirst();
            if (cartItem.isPresent()){
                int newQuantity = cartItem.get().getQuantity() + quantity;
                if (newQuantity <= product.getStock()){
                    instance.update(cart, productId, newQuantity);
                }
                if (newQuantity > product.getStock()) {
                    throw new OutOfStockException(product, product.getStock(), newQuantity);
                }
            } else {
                cart.getCartItems().add(new CartItem(product, quantity));
            }
        } catch (ProductNotFoundException exception) {
            exception.printStackTrace();
        }
        recalculateTotalCostAndQuantity(cart);
    }

    @Override
    public synchronized void update(Cart cart, Long productId, int quantity) throws OutOfStockException {
        if (quantity == 0) {
            delete(cart, productId);
            return;
        }
        Optional<CartItem> cartItem = cart.getCartItems().stream()
                        .filter(item -> item.getProduct().getId().equals(productId)).findFirst();
        if (cartItem.isPresent()){
            if (cartItem.get().getProduct().getStock() < quantity) {
                throw new OutOfStockException(cartItem.get().getProduct(), cartItem.get().getProduct().getStock(), quantity);
            }
            cartItem.get().setQuantity(quantity);
        }
        recalculateTotalCostAndQuantity(cart);
    }

    @Override
    public synchronized void delete(Cart cart, Long productId) {
        cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(cartItem -> cart.getCartItems().remove(cartItem));
        recalculateTotalCostAndQuantity(cart);
    }

    private void getTotalQuantity(Cart cart){
        int totalQuantity = cart.getCartItems().stream()
                .map(CartItem::getQuantity)
                .mapToInt(q -> q)
                .sum();
        cart.setTotalQuantity(totalQuantity);
    }
    private void getTotalCost(Cart cart){
        AtomicLong totalCost = new AtomicLong();
        cart.getCartItems().forEach(item ->
                totalCost.getAndAdd(item.getQuantity() * item.getProduct().getPrice().longValue()));
        cart.setTotalPrice(new BigDecimal(totalCost.get()));
    }

    private void recalculateTotalCostAndQuantity(Cart cart){
        getTotalQuantity(cart);
        getTotalCost(cart);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
