package org.example;

public class Book {
    private String title;
    private String author;
    private String publisher;
    private int publicationYear;
    private String genres;
    private int price;
    private String synopsis;
    private String content;

    public Book(String title, String author, String publisher, int publicationYear, String genres, int price, String synopsis, String content) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.genres = genres;
        this.price = price;
        this.synopsis = synopsis;
        this.content = content;
    }

    public String getTitle() { return title; }

    public String getAuthor() { return author; }

    public String getPublisher() { return publisher; }

    public int getPublicationYear() { return publicationYear; }

    public String getGenres() { return genres; }

    public int getPrice() { return price; }

    public String getSynopsis() { return synopsis; }

    public String getContent() { return content; }

    public void setTitle(String title) { this.title = title; }

    public void setAuthor(String author) { this.author = author; }

    public void setPublisher(String publisher) { this.publisher = publisher; }

    public void setPublicationYear(int publicationYear) { this.publicationYear = publicationYear; }

    public void setGenres(String genres) { this.genres = genres; }

    public void setPrice(int price) { this.price = price; }

    public void setSynopsis(String synopsis) { this.synopsis = synopsis; }

    public void setContent(String content) { this.content = content; }
}
