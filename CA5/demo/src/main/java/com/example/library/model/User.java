package com.example.library.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class User {
    @Getter
    private String username;
    private String password;
    @Getter
    private String email;
    @Getter
    private Address address;
    @Getter
    private String role;
    @Getter
    private int balance;
    @Getter
    private ArrayList<Order> shoppingCart;
    private final ArrayList<Order> borrowedBooks;
    private final ArrayList<Order> boughtBooks;
    @Getter
    private double payableAmount;
    @Getter
    private final ArrayList<Transaction> transactionHistory; // every arrayList is a history , start with books
                                                            // and three last elements are number of books and total price and time
                                                            // { books , numberOfBooks , totalPrice , Time }

    public User(String username, String password, String email, Address address, String role, int balance) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.role = role;
        this.balance = balance;
        this.shoppingCart = new ArrayList<>();
        this.transactionHistory = new ArrayList<Transaction>();
        this.borrowedBooks = new ArrayList<>();
        this.boughtBooks = new ArrayList<>();
    }

    public boolean authenticatePassword(String password) { return this.password.equals(password); }

    public void setUsername(String username) { this.username = username; }

    public void setPassword(String password) { this.password = password; }

    public void setEmail(String email) { this.email = email; }

    public void setAddress(Address address) { this.address = address; }

    public void setRole(String role) { this.role = role; }

    private Order findOrder(String bookTitle){
        for (Order order : this.shoppingCart) {
            if (order.getBook().getTitle().equals(bookTitle)) {
                return order;
            }
        }
        return null;
    }

    public void addTransactionHistory(Transaction newTransaction) {this.transactionHistory.add(newTransaction);}

    public void increaseBalance(int balanceToBeAdded) { this.balance += balanceToBeAdded; }

    public void setShoppingCart(ArrayList<Order> shoppingCart) { this.shoppingCart = shoppingCart; }

    public void addOrderToCart(Order orderToBeAddedToCart) {
        this.shoppingCart.add(orderToBeAddedToCart);
        if(orderToBeAddedToCart.getType().equals("buy"))
            this.increasePayableAmount(orderToBeAddedToCart.getBook().getPrice());
        else if(orderToBeAddedToCart.getType().equals("borrow"))
            this.increasePayableAmount((double) (orderToBeAddedToCart.getBook().getPrice() * orderToBeAddedToCart.getBorrowDurationDays()) / 10);
    }

    public void removeOrderFromCart(Book bookToBeRemovedFromCart) {
        Order orderToBeRemovedFromCart = findOrder(bookToBeRemovedFromCart.getTitle());
        this.shoppingCart.remove(orderToBeRemovedFromCart);
        assert orderToBeRemovedFromCart != null;
        if(orderToBeRemovedFromCart.getType().equals("buy"))
            this.decreasePayableAmount(orderToBeRemovedFromCart.getBook().getPrice());
        else if(orderToBeRemovedFromCart.getType().equals("borrow"))
            this.decreasePayableAmount((double) (orderToBeRemovedFromCart.getBook().getPrice() * orderToBeRemovedFromCart.getBorrowDurationDays()) / 10);
    }

    private void increasePayableAmount(double amount) { this.payableAmount += amount;}

    private void decreasePayableAmount(double amount) { this.payableAmount -= amount;}

    private void addPurchasedOrdersToShelf() {
        for (Order order : this.shoppingCart) {
            if ("buy".equals(order.getType())) {
                this.boughtBooks.add(order);
            } else if ("borrow".equals(order.getType())) {
                this.borrowedBooks.add(order);
            }
        }
    }

    private void makeTransactionHistory(){
        Transaction transaction = new Transaction(this.getShoppingCart(), this.getPayableAmount());
        this.addTransactionHistory(transaction);
    }

    public void updateInfoAfterCheckout() {
        this.makeTransactionHistory();
        this.addPurchasedOrdersToShelf();
        this.shoppingCart.clear();
        this.balance -= this.getPayableAmount();
        this.payableAmount = 0;
    }

    public boolean userHasAccessToBook(String bookTitle) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        for (Transaction transaction : this.getTransactionHistory()) {
            LocalDateTime transactionDate = LocalDateTime.parse(transaction.getTime(), formatter);

            for (Order order : transaction.getOrders()) {
                if (order == null) continue;

                long daysPassed = ChronoUnit.DAYS.between(transactionDate, now);
                int borrowDays = order.getBorrowDurationDays();

                boolean isOwned = (borrowDays == 0); // Purchased
                boolean isWithinBorrowTime = borrowDays > daysPassed;

                if (order.getBook().getTitle().equalsIgnoreCase(bookTitle)
                        && (isOwned || isWithinBorrowTime)) {
                    return true;
                }
            }
        }

        return false;
    }



}
