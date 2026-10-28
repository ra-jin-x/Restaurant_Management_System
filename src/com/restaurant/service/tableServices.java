package com.restaurant.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.restaurant.model.orderItem;
import com.restaurant.model.orders;
import com.restaurant.model.table;
import com.restaurant.payment.billSummary;
import com.restaurant.payment.payment;
import com.restaurant.payment.paymentService;
import com.restaurant.repo.tableRepo;

public class tableServices {
    private final tableRepo repo;
    private final List<orders> ordersList;
    private final List<orderItem> orderItems;
    private final List<String> menuItems;
    private int nextOrderId;
    private int nextItemId;

    public tableServices() {
        repo = new tableRepo();
        ordersList = new ArrayList<>();
        orderItems = new ArrayList<>();
        menuItems = new ArrayList<>(Arrays.asList(
                "Burger - 120.0",
                "Pizza - 300.0",
                "Fries - 60.0",
                "Tea - 40.0",
                "Pasta - 180.0"
        ));
        nextOrderId = 1;
        nextItemId = 1;
    }

    public List<table> getAll() {
        return repo.show();
    }

    public List<String> getMenuItems() {
        return menuItems;
    }

    public List<orders> getAllOrders() {
        return ordersList;
    }

    public List<orderItem> getAllOrderItems() {
        return orderItems;
    }

    public boolean selectTable(int tableId) {
        for (table currentTable : repo.show()) {
            if (currentTable.getId() == tableId) {
                currentTable.booking();
                return true;
            }
        }
        return false;
    }

    public double getTotalOrderBill() {
        return paymentService.calculateBillSummary(ordersList).getGrandTotal();
    }

    public billSummary getBillSummary() {
        return paymentService.calculateBillSummary(ordersList);
    }

    public payment processPayment(payment.PaymentMethod method, double amount) {
        return paymentService.processPayment(ordersList, method, amount);
    }

    public orders createOrder(int tableId, int menuIndex, int quantity) {
        if (menuIndex < 0 || menuIndex >= menuItems.size()) {
            return null;
        }
        if (quantity <= 0) {
            return null;
        }

        String selected = menuItems.get(menuIndex);
        String[] part = selected.split(" - ");
        String itemName = part[0];
        double price = Double.parseDouble(part[1]);

        orders newOrder = new orders(nextOrderId++, tableId, itemName, quantity, price);
        ordersList.add(newOrder);

        orderItem newItem = new orderItem(nextItemId++, newOrder.getId(), itemName, quantity, price);
        orderItems.add(newItem);

        return newOrder;
    }
}