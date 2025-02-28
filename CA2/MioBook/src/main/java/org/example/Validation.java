package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Validation {

    public boolean validateUsername(String username){
        return Pattern.matches("^[a-zA-Z0-9_-]+$", username);
    }

    public boolean validatePassword(String password){
        return password.length() < 4;
    }

    public boolean validateEmail(String email){
        return Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", email);
    }

    public boolean validateRole(String role){
        return role.equalsIgnoreCase("customer") && !role.equalsIgnoreCase("admin");
    }

    public boolean birthBeforeDeath(LocalDate deathDate, LocalDate birthDate){
        return birthDate.isBefore(deathDate);
    }

    public boolean minimumGenreCount(ArrayList<String> bookGenres){
        return bookGenres.size() > 1;
    }

    public boolean customerHasBookInCart(User customer, Book book){
        return customer.getShoppingCart().contains(book);
    }

    public boolean minimumCreditForBalanceCharge(int credit){
        return credit > 1000;
    }

    public boolean minimumBookCountInCartForCheckout(User customer){
        return customer.getShoppingCart().size() >= 1;
    }

    public boolean enoughBalanceForCheckout(User customer){
        return customer.getBalance() >= customer.getPayableAmount();
    }

    public boolean ratingInRange(int rating){
        return rating > 1 && rating < 5;
    }

    public boolean cartIsFull(User customer){
        return customer.getShoppingCart().size() == 10;
    }

}
