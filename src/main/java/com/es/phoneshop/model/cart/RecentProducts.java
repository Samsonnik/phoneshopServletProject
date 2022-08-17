package com.es.phoneshop.model.cart;

import com.es.phoneshop.product.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RecentProducts implements Serializable {

    private static List<Product> recentProductsList = new ArrayList<>(3);

    private RecentProducts() {
    }

    public static List<Product> getInstance() {
        return recentProductsList;
    }

    public static void addRecentProduct(Product product) {
        AtomicBoolean flag = new AtomicBoolean(true);
        recentProductsList.forEach(recentProduct -> {
            if (recentProduct.getCode().equals(product.getCode())){
                flag.set(false);
            }
        });
        if (!flag.get()) return;
        if (recentProductsList.size() == 3) {
            recentProductsList.remove(0);
        }
        if (product != null) {
            recentProductsList.add(product);
        }
    }

    public static void removeList(){
        recentProductsList.clear();
    }
}
