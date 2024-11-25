package com.Project.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PersistentData {


    private Seller sellerData;
    public final List<Product> Product;
    public final List<Order> orders;
    public boolean rememberMeStatus;
    private PersistentData() {
        this.sellerData = new Seller();
        this.Product = new ArrayList<>();
        this.orders = new ArrayList<>();
    }

    private static final class InstanceHolder {
        private static final PersistentData instance = new PersistentData();
    }

    public static PersistentData getInstance() {
        return InstanceHolder.instance;
    }

    public synchronized Seller getSeller() {
        return sellerData;
    }

    public synchronized void ClearSeller(){
        sellerData.name = "";
        sellerData.email = "";
        sellerData.password = "";
        sellerData.cif = "";
        sellerData.businessName = "";
        sellerData.phone = "";
    }
    public synchronized void loadProducts(List<Product> productList) {
        this.Product.clear();
        this.Product.addAll(productList);
    }

    public synchronized void loadOrders(List<Order> orderList) {
        this.orders.clear();
        this.orders.addAll(orderList);
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(Product);
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }
}
