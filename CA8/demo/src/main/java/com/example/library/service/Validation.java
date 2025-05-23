package com.example.library.service;

import com.example.library.entity.UserEntity;
import com.example.library.utils.PasswordUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Validation {


    public static boolean authenticatePassword(String givenPassword, UserEntity user) {
        String hashedInputPassword = PasswordUtils.hashPassword(givenPassword, user.getSalt());
        return hashedInputPassword.equals(user.getPassword());
    }
    public static boolean validateUsername(String username){
        return Pattern.matches("^[a-zA-Z0-9_-]+$", username);
    }
    public static boolean validatePassword(String password){
        return password.length() < 4;
    }
    public static boolean validateEmail(String email){
        return Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", email);
    }
    public static boolean validateRole(String role){
        return role.equalsIgnoreCase("customer") || role.equalsIgnoreCase("admin");
    }
    public static boolean birthBeforeDeath(LocalDate deathDate, LocalDate birthDate){
        return birthDate.isBefore(deathDate);
    }
    public static boolean minimumGenreCount(ArrayList<String> bookGenres){
        return bookGenres.size() > 1;
    }
    public static boolean minimumCreditForBalanceCharge(int credit){
        return credit >= 100;
    }
    public static boolean ratingInRange(int rating){
        return rating > 0 && rating <= 5;
    }
    /*public static boolean cartIsFull(User customer){
        return customer.getShoppingCart().size() >= 10;
    }*/

}
