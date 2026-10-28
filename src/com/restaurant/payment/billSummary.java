package com.restaurant.payment;

import java.util.ArrayList;
import java.util.List;

import com.restaurant.model.orders;

public class billSummary {
    private static final double DEFAULT_GST_RATE = 0.05;

    private double subtotal;
    private double gstRate;
    private double gstAmount;
    private double grandTotal;
    private final List<String> breakdown;

    public billSummary() {
        this(0.0, DEFAULT_GST_RATE, 0.0, 0.0);
    }

    public billSummary(double subtotal, double gstRate, double gstAmount, double grandTotal) {
        this.subtotal = subtotal;
        this.gstRate = gstRate;
        this.gstAmount = gstAmount;
        this.grandTotal = grandTotal;
        this.breakdown = new ArrayList<>();
    }

    public static billSummary fromOrders(List<orders> ordersList) {
        return fromOrders(ordersList, DEFAULT_GST_RATE);
    }

    public static billSummary fromOrders(List<orders> ordersList, double gstRate) {
        billSummary summary = new billSummary();
        summary.breakdown.clear();
        double total = 0.0;

        if (ordersList != null) {
            for (orders currentOrder : ordersList) {
                double lineTotal = currentOrder.getTotalPrice();
                total += lineTotal;
                summary.breakdown.add(
                        "Order " + currentOrder.getId() + " | " + currentOrder.getItemName()
                                + " | Qty: " + currentOrder.getQuantity() + " | Line Total: " + lineTotal
                );
            }
        }

        summary.subtotal = total;
        summary.gstRate = gstRate;
        summary.gstAmount = total * gstRate;
        summary.grandTotal = total + summary.gstAmount;
        return summary;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getGstRate() {
        return gstRate;
    }

    public void setGstRate(double gstRate) {
        this.gstRate = gstRate;
    }

    public double getGstAmount() {
        return gstAmount;
    }

    public void setGstAmount(double gstAmount) {
        this.gstAmount = gstAmount;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public List<String> getBreakdown() {
        return breakdown;
    }

    public void addBreakdownLine(String line) {
        breakdown.add(line);
    }
}
