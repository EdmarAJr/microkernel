package br.edu.ifba.inf008.model;

import java.io.Serializable;
import java.util.Random;

import static br.edu.ifba.inf008.persistence.DataPersistence.bookMap;

public class Book implements Serializable {
    private static int isbnIncrement = new Random().nextInt(100000);
    private int isbn;
    private String title;
    private String author;
    private String genre;
    private int year;
    private boolean isBorrowed;

    public Book(String title, String author, String genre, int year) {
        try {
            if (title.isEmpty() || author.isEmpty() || genre.isEmpty() || year <= 0)
                throw new IllegalArgumentException("TITLE, AUTHOR, GENRE AND YEAR CANNOT BE EMPTY FIELDS.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        if(bookMap.putIfAbsent(title.trim(), this) == null){
            this.isbn = ++isbnIncrement;
            this.title = title;
            this.author = author;
            this.genre = genre;
            this.year = year;
            this.isBorrowed = false;
        } else {
            System.out.println("BOOK ALREADY REGISTERED!");
        }
    }

    public int getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isBorrowed() { return isBorrowed; }
    public void borrow() { isBorrowed = true; }
    public void returnBook() { isBorrowed = false; }

    @Override
    public String toString() {
        return title + " by " + author + " (" + year + ") - " + (isBorrowed ? "Borrowed" : "Available");
    }

    public String getBookDetails() {
        String details = "ISBN: " + isbn + "\n" +
                "Title: " + title + "\n" +
                "Author: " + author + "\n" +
                "Genre: " + genre + "\n" +
                "Year: " + year + "\n" +
                "Status: " + (isBorrowed ? "Borrowed" : "Available") + "\n";
        return details;
    }
}