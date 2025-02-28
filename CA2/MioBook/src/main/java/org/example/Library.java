package org.example;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class Library {
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Author> authors = new ArrayList<>();
    private String success;
    private  String message;
    private List<Object> data;

    private boolean userExists(String username){
        return users.stream().anyMatch(user -> user.getUsername().equals(username));
    }

    private boolean bookExists(String bookTitle){
        return books.stream().anyMatch(book -> book.getTitle().equals(bookTitle));
    }

    private boolean authorExists(String name){
        return authors.stream().anyMatch(a -> a.getName().equals(name));
    }

    private boolean emailExists(String email){
        return users.stream().anyMatch(user -> user.getEmail().equals(email));
    }

    private boolean validateUsername(String username){
        return Pattern.matches("^[a-zA-Z0-9_-]+$", username);
    }

    private boolean validatePassword(String password){
        return password.length() < 4;
    }

    private boolean validateEmail(String email){
        return Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", email);
    }

    private boolean validateRole(String role){
        return role.equalsIgnoreCase("customer") && !role.equalsIgnoreCase("admin");
    }

    private boolean birthBeforeDeath(LocalDate deathDate, LocalDate birthDate){
        return birthDate.isBefore(deathDate);
    }

    private User findUser(String username){
        return this.users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    private Book findBook(String bookTitle){
        return this.books.stream()
                .filter(b -> b.getTitle().equals(bookTitle))
                .findFirst()
                .orElse(null);
    }

    public void addUser(String username, String password, String email, Address address, String role) {

        if (!validateUsername(username)) {
            message = "Invalid username! Only letters, numbers, underscore (_), and hyphen (-) are allowed.";
            success = "false";
            return;
        }


        if (userExists(username)) {
            message = "Username already exists! Please choose a different one.";
            success = "false";
            return;
        }

        if (emailExists(email)) {
            message = "Email address already registered! Please use a different one.";
            success = "false";
            return;
        }

        if (validatePassword(password)) {
            message = "Password must be at least 4 characters long!";
            success = "false";
            return;
        }

        if (!validateEmail(email)) {
            message = "Invalid email format! Example: example@test.com";
            success = "false";
            return;
        }

        if (!validateRole(role)) {
            message = "Invalid role! Role must be either 'customer' or 'admin'.";
            success = "false";
            return;
        }

        User newUser = new User(username, password, email, address, role.toLowerCase(), 0);
        users.add(newUser);

        message = "User successfully registered!";
        success = "true";
    }

    public void addBook (String username, String bookTitle,
                         String bookAuthor, String bookPublisher,
                         String publishYear, ArrayList<String> bookGenres,
                         String bookContent, String bookSynopsys, int bookPrice){

        if(!userExists(username)){
            message = "This username doesn't exist in system!";
            success = "false";
        }

        if(bookExists(bookTitle)){
            message = "This book already exist in system !";
            success = "false";
            return;
        }
        if(!authorExists(bookAuthor)){
            message = "The author of this book doesn't exist!";
            success = "false";
            return;
        }

        User u_user= findUser(username);
        if (!u_user.getRole().equals("admin")) {
            message = "Only an admin can add books!";
            success = "false";
            return;
        }

        try{
            Integer.parseInt(publishYear);
        } catch (NumberFormatException e) {
            message = "Publish year isn't in right format!";
            success = "false";
            return;
        }

        if(bookGenres.size()<1){
            message = "Book at least have one genres" ;
            success = "false" ;
            return;
        }

        Book newBook = new Book(bookTitle,bookAuthor,bookPublisher,Integer.parseInt(publishYear),
                bookGenres,bookPrice,bookSynopsys,bookContent);
        books.add(newBook);
        message =  "Book added successfully.";
        success = "true";


    }

    public void addBookToCart (String username, String bookTitle) {
        if(!userExists(username)){
            message = "User doesn't exist!";
            success = "false" ;
            return;
        }
        if(!bookExists(bookTitle)){
            message = "Book doesn't exist!";
            success = "false" ;
            return;
        }

        User customer = findUser(username);
        Book bookToBeAddedToCart = findBook(bookTitle);

        if (customer == null || bookToBeAddedToCart == null) return;

        if(customer.getRole().equals("admin")) {
            message = "You are an admin, you can't buy!";
            success = "false" ;
        }

        else if (customer.getShoppingCart().size() == 10) {
            message = "Your cart is full!";
            success = "false" ;
        }

        else {
            customer.addBookToCart(bookToBeAddedToCart);
            customer.incPayableAmount(bookToBeAddedToCart.getPrice());
            message = "Added book to cart.";
            success = "true";
        }

    }

    public void deleteBookFromCart(String username, String bookTitle){

        if(!userExists(username)){
            message = "User doesn't exist!";
            success = "false" ;
            return;
        }
        if(!bookExists(bookTitle)){
            message = "Book doesn't exist!";
            success = "false" ;
            return;
        }

        //User customer = users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
        //Book bookInCart = books.stream().filter(b -> b.getTitle().equals(bookTitle)).findFirst().orElse(null);

        User customer = findUser(username);
        Book bookToBeDeletedFromCart = findBook(bookTitle);

        if (customer == null || bookToBeDeletedFromCart == null) return;

        if(customer.getRole().equals("admin")) {
            message = "You are admin , you can't do this!";
            success = "false" ;
        }

        else if(!customer.getShoppingCart().contains(bookToBeDeletedFromCart)){
            message = "You don't have this book in your cart!";
            success = "false" ;
        }
        else {
            customer.deleteShoppingBook(bookToBeDeletedFromCart);
            message = "Book removed from cart successfully!";
            success = "true";
        }
    }

    public void addAuthor(String adminUsername, String authorName, String penName, String nationality, String birthDate, String deathDate) {

        if (!userExists(adminUsername)) {
            message = "User doesn't exist!";
            success = "false";
            return;
        }

        User adminUser = findUser(adminUsername);

        if (!adminUser.getRole().equals("admin")) {
            message = "Only an admin can add authors!";
            success = "false";
            return;
        }


        if (authorExists(authorName)) {
            message = "Author already exists!";
            success = "false";
            return;
        }

        LocalDate birthDateParsed;
        try {
            birthDateParsed = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (DateTimeParseException e) {
            message = "Invalid date of birth format! Use dd-MM-yyyy.";
            success = "false";
            return;
        }

        LocalDate deathDateParsed = null;
        if (deathDate != null && !deathDate.isEmpty()) {
            try {
                deathDateParsed = LocalDate.parse(deathDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } catch (DateTimeParseException e) {
                message = "Invalid date of death format! Use dd-MM-yyyy.";
                success = "false";
                return;
            }

            if (!birthBeforeDeath(deathDateParsed, birthDateParsed)) {
                message = "Date of death cannot be before date of birth!";
                success = "false";
                return;
            }
        }

        Author newAuthor = new Author(authorName, penName, nationality, birthDateParsed, deathDateParsed);
        authors.add(newAuthor);

        message = "Author added successfully!";
        success = "true";
    }

    public void addCredit(String username,int credit){
        if(!userExists(username)){
            message = "This username doesn't exist in system!";
            success = "false";
            return;
        }

        User customer = findUser(username);
        if(customer.getRole().equals("admin")) {
            message = "This activity isn't for admins";
            success = "false" ;
            return;
        }

        if(credit < 1000){
            message = "You should charge at least 1$ or 1000cent!";
            success = "false";
        }
        else {
            User u_user = findUser(username);
            u_user.increaseBalance(credit);
            message = "Credit added successfully.";
            success = "true" ;
        }
    }

    public void purchaseCard(String username){
        if(!userExists(username)){
            message = "this username doesn't exist in system";
            success = "false";
            return;
        }

        User u_user = findUser(username);
        if(u_user.getShoppingCart().size()<1){
            message = "Card don't have any book!";
            success = "false";
            return;
        }
        if(u_user.getBalance()<u_user.getPayableAmount()){
            message = "Balance isn't enough for purchase!";
            success = "false";
            return;
        }

        List<Object> transaction = new ArrayList<>();
        transaction.addAll(u_user.getShoppingCart());
        transaction.add(u_user.getShoppingCart().size());
        transaction.add(u_user.getPayableAmount());
        LocalDateTime purchaseTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = purchaseTime.format(formatter);
        transaction.add(formattedTime);
        u_user.addTransactionHistory(transaction);

        message = "Purchase completed successfully.";
        success = "true";
        data = transaction;

        u_user.clearCard();

    }

}
