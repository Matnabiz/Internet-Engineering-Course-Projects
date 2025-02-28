package org.example;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


public class Library {
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Author> authors = new ArrayList<>();
    private boolean success;
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

    private User findUser(String username){
        return this.users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    private Author findAuthor(String name){
        return this.authors.stream()
                .filter(u -> u.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    private Book findBook(String bookTitle){
        return this.books.stream()
                .filter(b -> b.getTitle().equals(bookTitle))
                .findFirst()
                .orElse(null);
    }

    public String addUser(String username, String password, String email, Address address, String role) {
        
        Validation validateData = null;

        if (!validateData.validateUsername(username)) {
            message = "Invalid username! Only letters, numbers, underscore (_), and hyphen (-) are allowed.";
            return OutputToJson.generateJson(false, message, null);
        }


        if (userExists(username)) {
            message = "Username already exists! Please choose a different one.";
            return OutputToJson.generateJson(false, message, null);
        }

        if (emailExists(email)) {
            message = "Email address already registered! Please use a different one.";
            return OutputToJson.generateJson(false, message, null);
        }

        if (validateData.validatePassword(password)) {
            message = "Password must be at least 4 characters long!";
            return OutputToJson.generateJson(false, message, null);
        }

        if (!validateData.validateEmail(email)) {
            message = "Invalid email format! Example: example@test.com";
            return OutputToJson.generateJson(false, message, null);
        }

        if (!validateData.validateRole(role)) {
            message = "Invalid role! Role must be either 'customer' or 'admin'.";
            return OutputToJson.generateJson(false, message, null);
        }

        User newUser = new User(username, password, email, address, role.toLowerCase(), 0);
        users.add(newUser);

        message = "User successfully registered!";
        return OutputToJson.generateJson(true, message, null);
    }

    public String addBook (String username, String bookTitle,
                         String bookAuthor, String bookPublisher,
                         String publishYear, ArrayList<String> bookGenres,
                         String bookContent, String bookSynopsys, int bookPrice){

        if(!userExists(username)){
            message = "This username doesn't exist in system!";
            return OutputToJson.generateJson(false, message, null);
        }

        if(bookExists(bookTitle)){
            message = "This book already exist in system !";
            return OutputToJson.generateJson(false, message, null);
        }
        if(!authorExists(bookAuthor)){
            message = "The author of this book doesn't exist!";
            return OutputToJson.generateJson(false, message, null);
        }

        User bookAdder= findUser(username);
        if (!bookAdder.getRole().equals("admin")) {
            message = "Only an admin can add books!";
            return OutputToJson.generateJson(false, message, null);
        }

        try{
            Integer.parseInt(publishYear);
        } catch (NumberFormatException e) {
            message = "Publish year isn't in right format!";
            return OutputToJson.generateJson(false, message, null);
        }

        Validation validateData = null;

        if(!validateData.minimumGenreCount(bookGenres)){
            message = "A Book should at least have one genre!" ;
            return OutputToJson.generateJson(false, message, null);
        }

        Book newBook = new Book(bookTitle, bookAuthor,
                bookPublisher, Integer.parseInt(publishYear),
                bookGenres,bookPrice,bookSynopsys,bookContent);
        books.add(newBook);
        message =  "Book added successfully.";
        return OutputToJson.generateJson(true, message, null);

    }

    public String addBookToCart (String username, String bookTitle) {

        if(!userExists(username)){
            message = "User doesn't exist!";
            return OutputToJson.generateJson(false, message, null);
        }

        if(!bookExists(bookTitle)){
            message = "Book doesn't exist!";
            return OutputToJson.generateJson(false, message, null);
        }

        User customer = findUser(username);
        Book bookToBeAddedToCart = findBook(bookTitle);

        if(customer.getRole().equals("admin")) {
            message = "You are an admin, you can't buy!";
            return OutputToJson.generateJson(false, message, null);
        }

        else if (customer.getShoppingCart().size() == 10) {
            message = "Your cart is full!";
            return OutputToJson.generateJson(false, message, null);
        }

        else {
            customer.addBookToCart(bookToBeAddedToCart);
            message = "Added book to cart.";
            return OutputToJson.generateJson(true, message, null);
        }

    }

    public String removeBookFromCart(String username, String bookTitle){

        if(!userExists(username)){
            message = "User doesn't exist!";
            return OutputToJson.generateJson(false, message, null);
        }

        if(!bookExists(bookTitle)){
            message = "Book doesn't exist!";
            return OutputToJson.generateJson(false, message, null);
        }

        User customer = findUser(username);
        Book bookToBeRemovedFromCart = findBook(bookTitle);
        Validation validateData = null;

        if(customer.getRole().equals("admin")) {
            message = "You are admin, you can't do this!";
            return OutputToJson.generateJson(false, message, null);
        }

        else if(!validateData.customerHasBookInCart(customer, bookToBeRemovedFromCart)){
            message = "You don't have this book in your cart!";
            return OutputToJson.generateJson(false, message, null);
        }

        else {
            customer.deleteBookFromCart(bookToBeRemovedFromCart);
            message = "Book removed from cart successfully!";
            return OutputToJson.generateJson(true, message, null);
        }
    }

    public String addAuthor(String adminUsername, String authorName, String penName, String nationality, String birthDate, String deathDate) {

        if (!userExists(adminUsername)) {
            message = "User doesn't exist!";
            return OutputToJson.generateJson(false, message, null);
        }

        User adminUser = findUser(adminUsername);

        if (!adminUser.getRole().equals("admin")) {
            message = "Only an admin can add authors!";
            return OutputToJson.generateJson(false, message, null);
        }

        if (authorExists(authorName)) {
            message = "Author already exists!";
            return OutputToJson.generateJson(false, message, null);
        }

        LocalDate birthDateParsed;
        try {
            birthDateParsed = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (DateTimeParseException e) {
            message = "Invalid date of birth format! Use dd-MM-yyyy.";
            return OutputToJson.generateJson(false, message, null);
        }

        LocalDate deathDateParsed = null;
        if (deathDate != null && !deathDate.isEmpty()) {
            try {
                deathDateParsed = LocalDate.parse(deathDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } catch (DateTimeParseException e) {
                message = "Invalid date of death format! Use dd-MM-yyyy.";
                return OutputToJson.generateJson(false, message, null);
            }

            Validation validateData = null;

            if (!validateData.birthBeforeDeath(deathDateParsed, birthDateParsed)) {
                message = "Date of death cannot be before date of birth!";
                return OutputToJson.generateJson(false, message, null);
            }
        }

        Author newAuthor = new Author(authorName, penName, nationality, birthDateParsed, deathDateParsed);
        authors.add(newAuthor);

        message = "Author added successfully!";
        return OutputToJson.generateJson(true, message, null);
    }

    public String addCredit(String username,int credit){
        if(!userExists(username)){
            message = "This username doesn't exist in system!";
            return OutputToJson.generateJson(false, message, null);
        }

        User customer = findUser(username);
        if(customer.getRole().equals("admin")) {
            message = "An admin can't add credit!";
            return OutputToJson.generateJson(false, message, null);
        }

        Validation validateData = null;

        if(!validateData.minimumCreditForBalanceCharge(credit)){
            message = "You should charge at least 1$ or 1000cent!";
            return OutputToJson.generateJson(false, message, null);
        }

        else {
            customer.increaseBalance(credit);
            message = "Credit added successfully.";
            return OutputToJson.generateJson(true, message, null);
        }
    }

    public String purchaseCart(String username){

        if(!userExists(username)){
            message = "this username doesn't exist in system";
            return OutputToJson.generateJson(false, message, null);
        }

        User customer = findUser(username);
        Validation validateData = null;

        if(!validateData.minimumBookCountInCartForCheckout(customer)){
            message = "Card don't have any book!";
            return OutputToJson.generateJson(false, message, null);
        }

        if(!validateData.enoughBalanceForCheckout(customer)){
            message = "Balance isn't enough for purchase!";
            return OutputToJson.generateJson(false, message, null);
        }

        List<Object> transaction = new ArrayList<>();
        transaction.addAll(customer.getShoppingCart());
        transaction.add(customer.getShoppingCart().size());
        transaction.add(customer.getPayableAmount());
        LocalDateTime purchaseTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = purchaseTime.format(formatter);
        transaction.add(formattedTime);
        customer.addTransactionHistory(transaction);

        message = "Purchase completed successfully.";
        Map<String, Object> userData = Map.of(
                "bookCount", customer.getShoppingCart().size(),
                "totalCost", customer.getPayableAmount(),
                "date", formattedTime
        );
        customer.updateInfoAfterCheckout();
        return OutputToJson.generateJson(true, message, userData);
    }

    public String addComment(String username, String bookTitle, String commentBody, int rating) {

        User customer = findUser(username);
        Book book = findBook(bookTitle);
        Validation validateData = null;

        if(!userExists(username)){
            message = "this username doesn't exist in system";
            return OutputToJson.generateJson(false, message, null);
        }

        if(customer.getRole().equals("admin")) {
            message = "An admin can't add credit!";
            return OutputToJson.generateJson(false, message, null);
        }

        if(!bookExists(bookTitle)){
            message = "Book doesn't exist!";
            return OutputToJson.generateJson(false, message, null);
        }

        if (!validateData.ratingInRange(rating)) {
            message = "Rating can only be a natural number between 1 and 5!";
            return OutputToJson.generateJson(false, message, null);
        }

        Comment newComment = new Comment(username, commentBody, rating);
        book.addComment(newComment);
        message = "Review added successfully.";
        return OutputToJson.generateJson(true, message, null);
    }

    public String showUserDetails(String username) {

        if(!userExists(username)){
            message = "this username doesn't exist in system";
            return OutputToJson.generateJson(false, message, null);
        }

        message = "User details retrieved successfully.\n";
        User user = findUser(username);
        Map<String, Object> userData = Map.of(
                "username", user.getUsername(),
                "email", user.getEmail(),
                "address", user.getAddress(),
                "role", user.getRole(),
                "balance", user.getBalance()
        );

        return OutputToJson.generateJson(true, message, userData);
    }

    public String showAuthorDetails(String authorName){

        if (!authorExists(authorName)) {
            message = "Author doesn't exist!";
            return OutputToJson.generateJson(false, message, null);
        }
        Author author = findAuthor(authorName);
        message = "Author details retrieved successfully.\n";
        Map<String, Object> authorData = Map.of(
                "name", author.getName(),
                "penName", author.getPenName(),
                "nationality", author.getNationality(),
                "born", author.getBirthDate(),
                "died", author.getDeathDate()
        );
        return OutputToJson.generateJson(true, message, authorData);
    }

    public String showBookDetails(String bookTitle){

        if (!bookExists(bookTitle)) {
            message = "Book doesn't exist!";
            return OutputToJson.generateJson(false, message, null);
        }

        Book book = findBook(bookTitle);
        message = "Book details retrieved successfully.\n";
        Map<String, Object> bookData = Map.of(
                "author", book.getAuthor(),
                "publisher", book.getPublisher(),
                "genres", book.getGenres(),
                "year", book.getPublicationYear(),
                "price", book.getPrice(),
                "averageRating", book.computeAverageRating()
        );
        return OutputToJson.generateJson(true, message, authorData);
    }

}
