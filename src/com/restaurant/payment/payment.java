package com.restaurant.payment;

import java.util.List;

import com.restaurant.model.orderItem;
import com.restaurant.model.orders;

public class payment {
    public enum PaymentMethod {
        CARD,
        CASH,
        UPI
    }

    private int id;
    private int orderId;
    private PaymentMethod method;
    private double amount;
    private double totalBill;
    private boolean verified;
    private String status;
    private billSummary billSummary;

    public payment() {
        this.status = "Pending";
        this.verified = false;
    }

    public payment(int id, int orderId, PaymentMethod method, double amount, double totalBill) {
        this.id = id;
        this.orderId = orderId;
        this.method = method;
        this.amount = amount;
        this.totalBill = totalBill;
        this.status = "Pending";
        this.verified = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(double totalBill) {
        this.totalBill = totalBill;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public billSummary getBillSummary() {
        return billSummary;
    }

    public void setBillSummary(billSummary billSummary) {
        this.billSummary = billSummary;
    }

    public boolean makePayment(PaymentMethod method, double amount, double totalBill) {
        this.method = method;
        this.amount = amount;
        this.totalBill = totalBill;

        if (amount < totalBill) {
            this.status = "Payment verification failed";
            this.verified = false;
            return false;
        }

        this.status = "Verified";
        this.verified = true;
        return true;
    }

    public boolean verifyPayment() {
        if (amount >= totalBill) {
            this.status = "Verified";
            this.verified = true;
            return true;
        }

        this.status = "Payment verification failed";
        this.verified = false;
        return false;
    }

    public void printBill(List<orders> ordersList, List<orderItem> orderItems) {
        if (billSummary == null) {
            billSummary = billSummary.fromOrders(ordersList);
        }

        System.out.println("\n===== RESTAURANT BILL =====");
        System.out.println("Payment method: " + method);
        System.out.println("Payment status: " + status);
        System.out.println("-------------------------");

        for (String line : billSummary.getBreakdown()) {
            System.out.println(line);
        }

        System.out.println("-------------------------");
        System.out.println("Subtotal: " + billSummary.getSubtotal());
        System.out.println("GST (" + billSummary.getGstRate() * 100 + "%): " + billSummary.getGstAmount());
        System.out.println("Grand total: " + billSummary.getGrandTotal());
        System.out.println("Paid amount: " + amount);
        System.out.println("Change: " + (amount - billSummary.getGrandTotal()));
        System.out.println("===========================");
        System.out.println("Thank you for visiting!");
    }
}
