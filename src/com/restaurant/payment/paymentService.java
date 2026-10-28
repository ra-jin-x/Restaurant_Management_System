package com.restaurant.payment;

import java.util.List;

import com.restaurant.model.orders;

public class paymentService {
    private static final double GST_RATE = 0.05;

    public static double calculateTotal(List<orders> ordersList) {
        double total = 0.0;
        for (orders currentOrder : ordersList) {
            total += currentOrder.getTotalPrice();
        }
        return total;
    }

    public static billSummary calculateBillSummary(List<orders> ordersList) {
        return billSummary.fromOrders(ordersList, GST_RATE);
    }

    public static payment processPayment(List<orders> ordersList, payment.PaymentMethod method, double amount) {
        billSummary summary = calculateBillSummary(ordersList);
        double totalBill = summary.getGrandTotal();
        payment newPayment = new payment(
                1,
                ordersList.isEmpty() ? 0 : ordersList.get(ordersList.size() - 1).getId(),
                method,
                amount,
                totalBill
        );
        newPayment.setBillSummary(summary);

        if (newPayment.makePayment(method, amount, totalBill)) {
            for (orders currentOrder : ordersList) {
                currentOrder.markPaid();
            }
            return newPayment;
        }

        return newPayment;
    }
}
