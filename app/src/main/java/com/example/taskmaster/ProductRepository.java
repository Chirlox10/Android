package com.example.taskmaster;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private static ProductRepository instance;
    private final List<String> activityLogs;
    private final List<Product> products;

    private ProductRepository() {
        activityLogs = new ArrayList<>();
        products = new ArrayList<>();
    }

    public static synchronized ProductRepository getInstance() {
        if (instance == null) {
            instance = new ProductRepository();
        }
        return instance;
    }

    public List<String> getActivityLogs() {
        return activityLogs;
    }

    public void addLog(String log) {
        activityLogs.add(0, log);
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }
}