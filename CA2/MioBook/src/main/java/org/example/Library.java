package org.example;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
                         int publishYear, ArrayList<String> bookGenres,
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

        Validation validateData = null;

        if(!validateData.minimumGenreCount(bookGenres)){
            message = "A Book should at least have one genre!" ;
            return OutputToJson.generateJson(false, message, null);
        }

        Book newBook = new Book(bookTitle, bookAuthor,
                bookPublisher, publishYear,
                bookGenres,bookPrice,bookSynopsys,bookContent);
        books.add(newBook);
        message =  "Book added successfully.";
        return OutputToJson.generateJson(true, message, null);

    }

    public String addOrderToCart (String username,String bookTitle, Order orderToBeAddedToCart) {

        if (!userExists(username)) {
            message = "User doesn't exist!";
            return OutputToJson.generateJson(false, message, null);
        }

        if (!bookExists(orderToBeAddedToCart.getBook().getTitle())) {
            message = "Book doesn't exist!";
            return OutputToJson.generateJson(false, message, null);
        }

        Book bookToBeAddedToCart = findBook(bookTitle);
        orderToBeAddedToCart.setBook(bookToBeAddedToCart);

        User customer = findUser(username);
        Validation validateData = null;
        if (customer.getRole().equals("admin")) {
            message = "You are an admin, you can't buy!";
            return OutputToJson.generateJson(false, message, null);
        } else if (validateData.cartIsFull(customer)) {
            message = "Your cart is full!";
            return OutputToJson.generateJson(false, message, null);
        }

        if (orderToBeAddedToCart.getType() == "buy") {
            customer.addOrderToCart(orderToBeAddedToCart);
            message = "Book added to cart.";
            return OutputToJson.generateJson(true, message, null);
        }

        else if (orderToBeAddedToCart.getType() == "borrow") {
            customer.addOrderToCart(orderToBeAddedToCart);
            message = "Book added to cart.";
            return OutputToJson.generateJson(true, message, null);
        }

        else {
            message = "Invalid purchase type.";
            return OutputToJson.generateJson(false, message, null);
        }
    }

    public String removeOrderFromCart(String username, String bookTitle){

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

        else if(!validateData.customerHasBookInCart(customer, bookToBeRemovedFromCart.getTitle())){
            message = "You don't have this book in your cart!";
            return OutputToJson.generateJson(false, message, null);
        }

        else {
            customer.removeOrderFromCart(bookToBeRemovedFromCart);
            message = "Book removed from cart successfully!";
            return OutputToJson.generateJson(true, message, null);
        }
    }

    public String addAuthor(String adminUsername, String authorName, String penName, String nationality, String birthDate, String deathDate) {
        System.out.println("Hi!");

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
        return OutputToJson.generateJson(true, message, bookData);
    }

    public String showBookReviews (String bookTitle){
        if(!bookExists(bookTitle)){
            message = "This book doesn't exist in system!\n";
            return OutputToJson.generateJson(false, message, null);
        }

        Book book = findBook(bookTitle);

        ArrayList<Map<String,Object>> reviews = new ArrayList<>();
        for(Comment comment : book.getComments()){
            reviews.add(Map.of("username",comment.getUsername(),"rate",comment.getRating(),"comment",comment.getBody()));
        }
        double averageRate = book.computeAverageRating();
        message = "Book reviews retrieved successfully.\n";

        Map<String, Object> bookData = Map.of(
                "title", book.getTitle(),
                "reviews", reviews,
                "averageRating", averageRate
        );
        return OutputToJson.generateJson(true, message, bookData);
    }

    public String searchBooksByTitle(String bookTitle){
        if(!bookExists(bookTitle)){
            message = "this book doesn't exist in system!";
            return OutputToJson.generateJson(false,message,null);
        }

        ArrayList<Book> searchedBook = (ArrayList<Book>) books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(bookTitle.toLowerCase()))
                .collect(Collectors.toList());

        ArrayList<Map<String,Object>> fBooks = new ArrayList<>();
        for(Book book : searchedBook){
            fBooks.add(Map.of("title",book.getTitle(),
                    "author",book.getAuthor(),
                    "publisher",book.getPublisher(),
                    "genres",book.getGenres(),
                    "year",book.getPublicationYear(),
                    "price",book.getPrice(),
                    "synopsis",book.getSynopsis()));
        }

        Map<String, Object> searchResult = Map.of(
                "search", bookTitle,
                "books",fBooks
        );
        message = "Books containing '" + bookTitle + "'  in their title:";
        return OutputToJson.generateJson(true,message,searchResult);
    }

    public String searchBooksByAuthor(String authorName){
        if(!authorExists(authorName)){
            message = "this author doesn't exist in system!";
            return OutputToJson.generateJson(false, message,null);
        }
        ArrayList<Book> searchedBook = (ArrayList<Book>) books.stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(authorName.toLowerCase()))
                .collect(Collectors.toList());

        ArrayList<Map<String,Object>> fBooks = new ArrayList<>();
        for(Book book : searchedBook){
            fBooks.add(Map.of("title",book.getTitle(),
                    "author",book.getAuthor(),
                    "publisher",book.getPublisher(),
                    "genres",book.getGenres(),
                    "year",book.getPublicationYear(),
                    "price",book.getPrice(),
                    "synopsis",book.getSynopsis()));
        }

        Map<String, Object> searchResult = Map.of(
                "search", authorName,
                "books",fBooks
        );
        message = "Books by "+authorName;
        return OutputToJson.generateJson(true,message,searchResult);
    }

    public String searchBooksByGenre (String genre){
        ArrayList<Book> searchedBook = (ArrayList<Book>) books.stream()
                .filter(book -> book.getGenres().contains(genre))
                .collect(Collectors.toList());

        ArrayList<Map<String,Object>> fBooks = new ArrayList<>();
        for(Book book : searchedBook){
            fBooks.add(Map.of("title",book.getTitle(),
                    "author",book.getAuthor(),
                    "publisher",book.getPublisher(),
                    "genres",book.getGenres(),
                    "year",book.getPublicationYear(),
                    "price",book.getPrice(),
                    "synopsis",book.getSynopsis()));
        }

        Map<String, Object> searchResult = Map.of(
                "search", genre,
                "books",fBooks
        );
        message = "Books in the '" + genre + "' genre:";
        return OutputToJson.generateJson(true,message,searchResult);
    }

    public String searchBooksByYear (int fromYear,int toYear){
        ArrayList<Book> searchedBook = (ArrayList<Book>) books.stream()
                .filter(book -> book.getPublicationYear() >= fromYear && book.getPublicationYear() <= toYear)
                .collect(Collectors.toList());

        ArrayList<Map<String,Object>> fBooks = new ArrayList<>();
        for(Book book : searchedBook){
            fBooks.add(Map.of("title",book.getTitle(),
                    "author",book.getAuthor(),
                    "publisher",book.getPublisher(),
                    "genres",book.getGenres(),
                    "year",book.getPublicationYear(),
                    "price",book.getPrice(),
                    "synopsis",book.getSynopsis()));
        }

        Map<String, Object> searchResult = Map.of(
                "search", fromYear+"-"+toYear,
                "books",fBooks
        );
        message = "Books published from '"+fromYear+"' to '"+toYear+"':";
        return OutputToJson.generateJson(true,message,searchResult);
    }

    public String showBookContent(String username, String bookTitle){
        if (!userExists(username)) {
            message = "User doesn't exist!";
            return OutputToJson.generateJson(false, message, null);
        }

        if (!bookExists(bookTitle)) {
            message = "Book doesn't exist!";
            return OutputToJson.generateJson(false, message, null);
        }



        message = "Book Content retrieved successfully.\n";
        Book bookToBeShown = findBook(bookTitle);
        Map<String, Object> bookContent = Map.of(
                "title", bookTitle,
                "content", bookToBeShown.getContent()
        );
        return OutputToJson.generateJson(true, message, bookContent);

    }

    public String showCart(String username){
        if(!userExists(username)){
            message = "User doesn't exist";
            return OutputToJson.generateJson(false,message,null);
        }

        User u_user= findUser(username);
        if (u_user.getRole().equals("admin")) {
            message = "This command isn't for admins!";
            return OutputToJson.generateJson(false, message, null);
        }

        ArrayList<Map<String,Object>> bookInCard = new ArrayList<>();
        for(Order order : u_user.getShoppingCart()){
            bookInCard.add(Map.of("title",order.getBook().getTitle(),
                    "author",order.getBook().getAuthor(),
                    "publisher",order.getBook().getPublisher(),
                    "genres",order.getBook().getGenres(),
                    "year",order.getBook().getPublicationYear(),
                    "price",order.getBook().getPrice(),
                    "isBorrowed",order.getType().equals("borrow"),
                    "finalPrice",order.getOrderPrice(),
                    "borrowDays",order.getBorrowDurationDays()));
        }

        Map<String, Object> cartDetails = Map.of(
                "username", username,
                "totalCost",u_user.getPayableAmount(),
                "items",bookInCard
        );
        message="Buy cart retrieved successfully.";
        return OutputToJson.generateJson(true,message,cartDetails);

    }

    public String showPurchaseHistory(String username) {

        if (!userExists(username)) {
            message = "User doesn't exist";
            return OutputToJson.generateJson(false, message, null);
        }

        User u_user = findUser(username);
        if (u_user.getRole().equals("admin")) {
            message = "This command isn't for admins!";
            return OutputToJson.generateJson(false, message, null);
        }

        ArrayList<Map<String, Object>> bookItems = new ArrayList<>();
        for (int i = 0; i < u_user.getTransactionHistory().size() - 3; i++) {
            if (u_user.getTransactionHistory().get(i) instanceof Order) {
                Order order = (Order) u_user.getTransactionHistory().get(i); // Cast to Book
                bookItems.add(Map.of("title", order.getBook().getTitle(),
                        "author", order.getBook().getAuthor(),
                        "publisher", order.getBook().getPublisher(),
                        "genres", order.getBook().getGenres(),
                        "year", order.getBook().getPublicationYear(),
                        "isBorrowed", order.getType().equals("borrow"),
                        "price", order.getBook().getPrice(),
                        "finalPrice", order.getOrderPrice()));
            }
        }

        Map<String, Object> purchaseHistory = Map.of(
                "purchaseDate", u_user.getTransactionHistory().get(u_user.getTransactionHistory().size()- 1),
                "items", bookItems,
                "totalCost", u_user.getTransactionHistory().get(u_user.getTransactionHistory().size() - 2));


        Map<String, Object> finalPurchaseHistory = Map.of(
                "username", username,
                "purchaseHistory", purchaseHistory
        );

        message = "Purchase history retrieved successfully.";
        return OutputToJson.generateJson(true, message, finalPurchaseHistory);
    }

    public String showPurchasedBook (String username){
        if (!userExists(username)) {
            message = "User doesn't exist";
            return OutputToJson.generateJson(false, message, null);
        }

        User u_user = findUser(username);
        if (u_user.getRole().equals("admin")) {
            message = "This command isn't for admins!";
            return OutputToJson.generateJson(false, message, null);
        }

        ArrayList<Map<String, Object>> purchasedBook = new ArrayList<>();
        for (int i = 0; i < u_user.getTransactionHistory().size() - 3; i++) {
            if (u_user.getTransactionHistory().get(i) instanceof Order) {
                Order order = (Order) u_user.getTransactionHistory().get(i);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime givenDate = LocalDateTime.parse(String.valueOf(u_user.getTransactionHistory().get(u_user.getTransactionHistory().size() - 1)));
                LocalDateTime now = LocalDateTime.now();
                long daysPassed = ChronoUnit.DAYS.between(givenDate, now);
                if (order.getBorrowDurationDays() < daysPassed )  { // ----> check for bought book not borrowed
                    purchasedBook.add(Map.of("title", order.getBook().getTitle(),
                            "author", order.getBook().getAuthor(),
                            "publisher", order.getBook().getPublisher(),
                            "category", order.getBook().getGenres(),
                            "year", order.getBook().getPublicationYear(),
                            "price", order.getBook().getPrice(),
                            "isBorrowed", order.getType().equals("borrow")));
                }
            }
        }

        Map<String, Object> finalPurchasedBook = Map.of(
                "username", username,
                "books", purchasedBook
        );
        message="Purchased books retrieved successfully.";
        return OutputToJson.generateJson(true,message,finalPurchasedBook);
    }

}
