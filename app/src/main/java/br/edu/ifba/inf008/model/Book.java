package br.edu.ifba.inf008.model;

/*import java.io.Serializable;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.ArrayList;*/

import java.io.Serializable;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
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

//    public String getBookDetails() {
//        StringBuilder details = new StringBuilder();
//        details.append("ISBN: ").append(isbn).append("\n")
//                .append("Title: ").append(title).append("\n")
//                .append("Author: ").append(author).append("\n")
//                .append("Genre: ").append(genre).append("\n")
//                .append("Year: ").append(year).append("\n")
//                .append("Status: ").append(isBorrowed ? "Borrowed" : "Available");
//        return details.toString();
//    }

    public String getBookDetails() {
        String details = "ISBN: " + isbn + "\n" +
                "Title: " + title + "\n" +
                "Author: " + author + "\n" +
                "Genre: " + genre + "\n" +
                "Year: " + year + "\n" +
                "Status: " + (isBorrowed ? "Borrowed" : "Available") + "\n";
        return details;
    }

    public static void saveBooks(List<Book> books, String fileName) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(books);
        }
    }

    public static List<Book> loadBooks(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (List<Book>) ois.readObject();
        }
    }



    //    public String getIsbn() {
//        return isbn;
//    }
//
//    public void setIsbn(String isbn) {
//        this.isbn = isbn;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public void setAuthor(String author) {
//        this.author = author;
//    }


}