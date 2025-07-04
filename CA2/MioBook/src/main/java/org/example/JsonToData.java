package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class JsonToData {
    private Library library;

    public JsonToData(Library library) {
        this.library = library;
    }

    public void processInput(String input) {
        try {
            int firstSpace = input.indexOf(" ");
            if (firstSpace == -1) {
                System.out.println("Invalid input format!");
                return;
            }

            String command = input.substring(0, firstSpace);
            String jsonString = input.substring(firstSpace + 1);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonString);

            switch (command) {
                case "add_author":
                    handleAddAuthor(rootNode);
                    break;
                case "add_user":
                    handleAddUser(rootNode);
                    break;
                case "add_book":
                    handleAddBook(rootNode);
                    break;
                case "add_cart":
                    handleAddCart(rootNode);
                    break;
                case "remove_cart":
                    handleRemoveCart(rootNode);
                    break;
                case "add_credit":
                    handleAddCredit(rootNode);
                    break;
                case "purchase_cart":
                    handlePurchaseCart(rootNode);
                    break;
                case "borrow_book":
                    handleBorrowBook(rootNode);
                    break;
                case "add_review":
                    handleAddComment(rootNode);
                    break;
                case "show_user_details":
                    handleShowUserDetails(rootNode);
                    break;
                case "show_author_details":
                    handleShowAuthorDetails(rootNode);
                    break;
                case "show_book_details":
                    handleShowBookDetails(rootNode);
                    break;
                case "show_book_content":
                    handleShowBookContent(rootNode);
                    break;
                case "show_book_reviews":
                    handleShowBookReviews(rootNode);
                    break;
                case "show_cart":
                    handleShowCart(rootNode);
                    break;
                case "show_purchase_history":
                    handleShowPurchaseHistory(rootNode);
                    break;
                case "show_purchased_books":
                    handleShowPurchasedBooks(rootNode);
                    break;
                case "search_books_by_title":
                    handleSearchBooksByTitle(rootNode);
                    break;
                case "search_books_by_year":
                    handleSearchBooksByYear(rootNode);
                    break;
                case "search_books_by_author":
                    handleSearchBooksByAuthor(rootNode);
                    break;
                case "search_books_by_genre":
                    handleSearchBooksByGenre(rootNode);
                    break;
                default:
                    System.out.println("Invalid command: " + command);
            }

        } catch (Exception e) {
            System.out.println("Error processing input: " + e.getMessage());
        }
    }

    private void handleAddAuthor(JsonNode rootNode) {
        String username = rootNode.get("username").asText();
        String name = rootNode.get("name").asText();
        String penName = rootNode.has("penName") ? rootNode.get("penName").asText() : null;
        String nationality = rootNode.has("nationality") ? rootNode.get("nationality").asText() : null;
        String born = rootNode.get("born").asText();
        String died = rootNode.has("died") ? rootNode.get("died").asText() : null;


        library.addAuthor(username, name, penName, nationality, born, died);
    }

    private void handleAddUser(JsonNode rootNode) {
        String username = rootNode.get("username").asText();
        String password = rootNode.get("password").asText();
        String email = rootNode.get("email").asText();
        String country = rootNode.get("address").get("country").asText();
        String city = rootNode.get("address").get("city").asText();
        String role = rootNode.get("role").asText();

        Address address = new Address(country, city);
        library.addUser(username, password, email, address, role);
    }

    private void handleAddBook(JsonNode rootNode) {
        String username = rootNode.get("username").asText();
        String title = rootNode.get("title").asText();
        String author = rootNode.get("author").asText();
        int price = rootNode.get("price").asInt();
        int publicationYear = rootNode.get("year").asInt();
        String publisher = rootNode.get("publisher").asText();
        ArrayList<String> genres = new ArrayList<>();
        JsonNode genresNode = rootNode.get("genres");

        if (genresNode != null && genresNode.isArray()) {
            for (JsonNode genre : genresNode) {
                genres.add(genre.asText());
            }
        }
        String content = rootNode.get("content").asText();
        String synopsis = rootNode.get("synopsis").asText();


        library.addBook(username, title, author, publisher, publicationYear, genres, content, synopsis, price);
    }

    private void handleBorrowBook(JsonNode rootNode) {
        String username = rootNode.get("username").asText();
        String bookTitle = rootNode.get("title").asText();
        int borrowDurationDays = rootNode.get("days").asInt();

        Order orderToBeAddedToCart = new Order(null, "borrow", borrowDurationDays);
        library.addOrderToCart(username, bookTitle, orderToBeAddedToCart);
    }

    private void handleAddCart(JsonNode rootNode) {
        String username = rootNode.get("username").asText();
        String bookTitle = rootNode.get("title").asText();

        Order orderToBeAddedToCart = new Order(null, "buy", 0);
        library.addOrderToCart(username, bookTitle, orderToBeAddedToCart);
    }

    private void handleRemoveCart(JsonNode rootNode) {
        String username = rootNode.get("username").asText();
        String bookTitle = rootNode.get("title").asText();

        library.removeOrderFromCart(username, bookTitle);
    }

    private void handleAddCredit(JsonNode rootNode) {
        String username = rootNode.get("username").asText();
        int credit  = rootNode.get("credit").asInt();

        library.addCredit(username, credit);
    }

    private void handleShowUserDetails(JsonNode rootNode) {
        String username = rootNode.get("username").asText();

        library.showUserDetails(username);
    }

    private void handleShowAuthorDetails(JsonNode rootNode) {
        String authorName = rootNode.get("name").asText();
        library.showAuthorDetails(authorName);
    }

    private void handleShowBookDetails(JsonNode rootNode) {
        String bookTitle = rootNode.get("title").asText();
        library.showBookDetails(bookTitle);
    }

    private void handleShowCart(JsonNode rootNode) {
        String username = rootNode.get("username").asText();
        library.showCart(username);
    }

    private void handleSearchBooksByTitle(JsonNode rootNode) {
        String bookTitle = rootNode.get("title").asText();
        library.searchBooksByTitle(bookTitle);
    }

    private void handleSearchBooksByAuthor(JsonNode rootNode) {
        String bookAuthor = rootNode.get("name").asText();
        library.searchBooksByAuthor(bookAuthor);
    }

    private void handleSearchBooksByGenre(JsonNode rootNode) {
        String genre = rootNode.get("genre").asText();
        library.searchBooksByGenre(genre);
    }

    private void handleSearchBooksByYear(JsonNode rootNode) {
        int fromYear = rootNode.get("from").asInt();
        int toYear = rootNode.get("to").asInt();
        library.searchBooksByYear(fromYear, toYear);
    }

    private void handleShowBookContent(JsonNode rootNode) {
        String username = rootNode.get("username").asText();
        String bookTitle = rootNode.get("title").asText();
        library.showBookContent(username, bookTitle);
    }

    private void handleShowPurchaseHistory(JsonNode rootNode) {
        String username = rootNode.get("username").asText();
        library.showPurchaseHistory(username);
    }

    private void handleShowBookReviews(JsonNode rootNode) {
        String bookTitle = rootNode.get("title").asText();

        library.showBookReviews(bookTitle);
    }

    private void handleShowPurchasedBooks(JsonNode rootNode) {
        String username = rootNode.get("username").asText();
        library.showPurchasedBooks(username);
    }

    private void handleAddComment(JsonNode rootNode) {
        String username = rootNode.get("username").asText();
        String bookTitle = rootNode.get("title").asText();
        String comment = rootNode.get("comment").asText();
        int rating = rootNode.get("rate").asInt();

        library.addComment(username, bookTitle, comment, rating);
    }

    private void handlePurchaseCart(JsonNode rootNode) {
        String username = rootNode.get("username").asText();
        library.purchaseCart(username);
    }
}
