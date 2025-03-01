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

            String command = input.substring(0, firstSpace); // Extract command (e.g., add_author)
            String jsonString = input.substring(firstSpace + 1); // Extract JSON data

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
                case "add_comment":
                    handleAddComment(rootNode);
                    break;
                case "checkout":
                    handleCheckout(rootNode);
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
        int publicationYear = rootNode.get("publicationYear").asInt();
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

    private void handleAddComment(JsonNode rootNode) {
        String username = rootNode.get("username").asText();
        String bookTitle = rootNode.get("bookTitle").asText();
        String comment = rootNode.get("comment").asText();
        int rating = rootNode.get("rating").asInt();

        library.addComment(username, bookTitle, comment, rating);
    }

    private void handleCheckout(JsonNode rootNode) {
        String username = rootNode.get("username").asText();
        library.purchaseCart(username);
    }
}
