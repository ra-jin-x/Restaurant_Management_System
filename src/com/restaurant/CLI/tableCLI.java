package com.restaurant.CLI;

import java.util.List;
import java.util.Scanner;

import com.restaurant.model.orderItem;
import com.restaurant.model.orders;
import com.restaurant.model.table;
import com.restaurant.payment.billSummary;
import com.restaurant.payment.payment;
import com.restaurant.service.tableServices;

public class tableCLI {
    private final tableServices service;
    private final Scanner scanner;

    public tableCLI() {
        this.service = new tableServices();
        this.scanner = new Scanner(System.in);
    }

    public tableCLI(tableServices service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        int option = 0;

        do {
            try {
                System.out.println("\nRestaurant Menu");
                System.out.println("1. Show all tables");
                System.out.println("2. Create order");
                System.out.println("3. Show all orders");
                System.out.println("4. Show all order items");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                option = readInt();

                switch (option) {
                    case 1:
                        showTables();
                        break;
                    case 2:
                        createOrderFlow();
                        break;
                    case 3:
                        showOrders();
                        break;
                    case 4:
                        showOrderItems();
                        break;
                    case 5:
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
                option = 0;
            }
        } while (option != 5);
    }

    private void showTables() {
        List<table> tables = service.getAll();
        for (table currentTable : tables) {
            System.out.println("Table " + currentTable.getId()
                    + " | Capacity: " + currentTable.getCapacity()
                    + " | Status: " + currentTable.getStatus());
        }
    }

    private void createOrderFlow() {
        try {
            System.out.println("\nAvailable tables");
            showTables();

            System.out.print("Select a table id: ");
            int selectedTableId = readInt();

            if (!service.selectTable(selectedTableId)) {
                System.out.println("Invalid table selection.");
                return;
            }

            System.out.println("\nMenu Items");
            List<String> menuItems = service.getMenuItems();
            for (int i = 0; i < menuItems.size(); i++) {
                System.out.println((i + 1) + ". " + menuItems.get(i));
            }

            System.out.print("Choose menu item number: ");
            int menuChoice = readInt();

            System.out.print("Enter quantity: ");
            int quantity = readInt();

            orders createdOrder = service.createOrder(selectedTableId, menuChoice - 1, quantity);
            if (createdOrder == null) {
                System.out.println("Invalid item or quantity.");
                return;
            }

            System.out.println("\nOrder created successfully.");
            System.out.println("Order " + createdOrder.getId()
                    + " | Table: " + createdOrder.getTableId()
                    + " | Item: " + createdOrder.getItemName()
                    + " | Qty: " + createdOrder.getQuantity()
                    + " | Price: " + createdOrder.getPrice()
                    + " | Status: " + createdOrder.getStatus()
                    + " | Total: " + createdOrder.getTotalPrice());

            System.out.println("\nWhat next?");
            System.out.println("1. Another order");
            System.out.println("2. Payment and exit");
            System.out.println("3. Exit without payment");
            System.out.print("Enter your choice: ");
            int nextAction = readInt();

            switch (nextAction) {
                case 1:
                    createOrderFlow();
                    break;
                case 2:
                    handlePayment();
                    return;
                case 3:
                    showOrders();
                    showOrderItems();
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid action. Exiting.");
                    System.out.println("Goodbye!");
                    return;
            }
        } catch (Exception e) {
            System.out.println("Invalid input during order creation.");
            scanner.nextLine();
        }
    }

    private void handlePayment() {
        try {
            System.out.println("\nPayment Page");
            System.out.println("1. Make a payment");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");
            int paymentChoice = readInt();

            if (paymentChoice == 2) {
                System.out.println("Goodbye!");
                return;
            }

            if (paymentChoice != 1) {
                System.out.println("Invalid payment choice.");
                System.out.println("Goodbye!");
                return;
            }

            billSummary summary = service.getBillSummary();
            if (summary == null || summary.getBreakdown().isEmpty()) {
                System.out.println("No bill available to pay.");
                return;
            }

            System.out.println("\nBill Summary");
            for (String line : summary.getBreakdown()) {
                System.out.println(line);
            }
            System.out.println("Subtotal: " + summary.getSubtotal());
            System.out.println("GST (" + (summary.getGstRate() * 100) + "%): " + summary.getGstAmount());
            System.out.println("Grand total: " + summary.getGrandTotal());

            System.out.println("\nChoose payment method:");
            System.out.println("1. Card");
            System.out.println("2. Cash");
            System.out.println("3. UPI");
            System.out.print("Enter your payment method: ");
            int methodChoice = readInt();

            payment.PaymentMethod method = null;
            switch (methodChoice) {
                case 1:
                    method = payment.PaymentMethod.CARD;
                    break;
                case 2:
                    method = payment.PaymentMethod.CASH;
                    break;
                case 3:
                    method = payment.PaymentMethod.UPI;
                    break;
                default:
                    System.out.println("Invalid payment method.");
                    return;
            }

            System.out.print("Enter amount paid: ");
            double amountPaid = readDouble();

            payment paid = service.processPayment(method, amountPaid);
            if (paid == null || !paid.isVerified()) {
                System.out.println("Payment verification failed. Please try again.");
                return;
            }

            System.out.println("Payment verified successfully.");
            paid.setBillSummary(summary);
            paid.printBill(service.getAllOrders(), service.getAllOrderItems());
            showOrders();
            showOrderItems();
            System.out.println("Goodbye!");
        } catch (Exception e) {
            System.out.println("Invalid input during payment.");
            scanner.nextLine();
        }
    }

    private int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
                System.out.print("Try again: ");
            }
        }
    }

    private double readDouble() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid decimal amount.");
                System.out.print("Try again: ");
            }
        }
    }

    private void showOrders() {
        List<orders> ordersList = service.getAllOrders();
        if (ordersList.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        System.out.println("\nAll Orders");
        for (orders currentOrder : ordersList) {
            System.out.println("Order " + currentOrder.getId()
                    + " | Table: " + currentOrder.getTableId()
                    + " | Item: " + currentOrder.getItemName()
                    + " | Qty: " + currentOrder.getQuantity()
                    + " | Price: " + currentOrder.getPrice()
                    + " | Status: " + currentOrder.getStatus()
                    + " | Total: " + currentOrder.getTotalPrice());
        }
    }

    private void showOrderItems() {
        List<orderItem> orderItems = service.getAllOrderItems();
        if (orderItems.isEmpty()) {
            System.out.println("No order items found.");
            return;
        }

        System.out.println("\nAll Order Items");
        for (orderItem currentItem : orderItems) {
            System.out.println("OrderItem " + currentItem.getId()
                    + " | Order: " + currentItem.getOrderId()
                    + " | Item: " + currentItem.getItemName()
                    + " | Qty: " + currentItem.getQuantity()
                    + " | Unit Price: " + currentItem.getUnitPrice()
                    + " | Total: " + currentItem.getTotalPrice());
        }
    }
}
