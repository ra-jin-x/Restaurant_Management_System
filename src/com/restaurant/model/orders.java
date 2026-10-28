package com.restaurant.model;

public class orders {
    public enum OrderStatus {
        pending,
        preparing,
        served,
        paid,
        cancelled
    }

    private int id;
    private int tableId;
    private String itemName;
    private int quantity;
    private double price;
    private OrderStatus status;

    public orders() {
        this.status = OrderStatus.pending;
    }

    public orders(int id, int tableId, String itemName, int quantity, double price) {
        this.id = id;
        this.tableId = tableId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.status = OrderStatus.pending;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return price * quantity;
    }

    public void markPreparing() {
        this.status = OrderStatus.preparing;
    }

    public void markServed() {
        this.status = OrderStatus.served;
    }

    public void markPaid() {
        this.status = OrderStatus.paid;
    }

    public void cancel() {
        this.status = OrderStatus.cancelled;
    }

    @Override
    public String toString() {
        return "orders{" +
                "id=" + id +
                ", tableId=" + tableId +
                ", itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", status=" + status +
                '}';
    }
}