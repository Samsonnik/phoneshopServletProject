package com.es.phoneshop.dao;

import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.product.Product;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {

    private long maxId;
    private List<Product> products;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private static ProductDao instance;

    private ArrayListProductDao() {
        this.products = new ArrayList<>();
    }

    public static synchronized ProductDao getInstance() {
        if (instance == null) {
            instance = new ArrayListProductDao();
        }
        return instance;
    }

    @Override
    public Product getProduct(Long id) throws ProductNotFoundException {
        readWriteLock.readLock().lock();
        try {
            return products.stream()
                    .filter(product -> product.getId().equals(id))
                    .findFirst()
                    .orElseThrow(ProductNotFoundException::new);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        readWriteLock.writeLock().lock();
        try {
            products = sortByPriceStockAndGetSorting(sortField, sortOrder);
            Comparator<Product> comparatorForQuery = Comparator.comparingInt(products ->
                    getMatchingValue(products, query));

            if ((query == null) || (query.equals("") || (query.equals(" ")))) {
                return new ArrayList<>(products);
            }
            return products.stream()
                    .sorted(comparatorForQuery.reversed())
                    .filter(product -> getMatchingValue(product, query) > 0)
                    .collect(Collectors.toList());
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private Comparator<Product> getComparatorForSorting(SortField sortField, SortOrder sortOrder) {
        Comparator<Product> comparatorForSorting;
        comparatorForSorting = Comparator.comparing(product -> {
            if (SortField.description == sortField) {
                return (Comparable) product.getDescription();
            } else {
                return (Comparable) product.getPrice();
            }
        });
        if (SortOrder.asc == sortOrder) {
            comparatorForSorting = comparatorForSorting.reversed();
        }
        return comparatorForSorting;
    }

    private List<Product> sortByPriceStockAndGetSorting(SortField sortField, SortOrder sortOrder) {
        return products.stream()
                .filter(this::productIsOnStock)
                .filter(this::productHasCorrectPrice)
                .sorted(getComparatorForSorting(sortField, sortOrder))
                .collect(Collectors.toList());
    }


    private int getMatchingValue(Product product, String query) {
        readWriteLock.writeLock().lock();
        AtomicInteger matchValue = new AtomicInteger();
        Arrays.stream(product.getDescription().split(" "))
                .forEach(descriptionWord -> Arrays.stream(query.split(" ")).forEach(queryWord -> {
                    if (descriptionWord.equalsIgnoreCase(queryWord)) {
                        matchValue.getAndIncrement();
                    }
                }));
        readWriteLock.writeLock().unlock();
        return matchValue.get();
    }

    public boolean productIsOnStock(Product product) {
        return product.getStock() > 0;
    }

    private boolean productHasCorrectPrice(Product product) {
        return product.getPrice() != null;
    }

    @Override
    public void save(Product product) {
        readWriteLock.writeLock().lock();
        try {
            if (product == null) return;
            if (product.getStock() <= 0) return;
            if (product.getId() == null) {
                product.setId(maxId++);
                products.add(product);
            }
            if (product.getId() != null) {
                Product result;
                try {
                    result = getProduct(product.getId());
                } catch (ProductNotFoundException exception) {
                    products.add(product);
                    return;
                }
                products.remove(result);
                products.add(product);
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void delete(Long id) {
        readWriteLock.writeLock().lock();
        try {
            products.removeIf(product -> product.getId().equals(id));
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
}
