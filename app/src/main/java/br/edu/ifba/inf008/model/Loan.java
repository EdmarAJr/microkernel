package br.edu.ifba.inf008.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static br.edu.ifba.inf008.persistence.DataPersistence.bookMap;
import static br.edu.ifba.inf008.persistence.DataPersistence.loanMap;

public class Loan implements Serializable {
    private int loanId;
    private static int loanIdIncrement = 0;
    private Reader reader;
    private Book book;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate retunDate;

    public Loan(Reader reader, Book book, LocalDate loanDate) {
        try {
            if (reader == null || book == null || loanDate == null)
                throw new IllegalArgumentException("READER, BOOK AND LOAN DATE CANNOT BE EMPTY FIELDS.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        if (reader != null && book != null && loanDate != null) {
            this.loanId = ++loanIdIncrement;
            this.reader = reader;
            this.book = book;
            this.loanDate = loanDate;
            this.dueDate = loanDate.plusDays(14);
            if (bookMap.get(book.getTitle()) != null) {
                if (bookMap.get(book.getTitle()).isBorrowed())
                    System.out.println("BOOK ALREADY BORROWED!");
                else
                    bookMap.get(book.getTitle()).borrow();
                retunDate = null;
            } else
                System.out.println("BOOK NOT REGISTERED!");
        } else
            System.out.println("LOAN FAILURE!");

    }

    public int getLoanId() { return loanId; }

    public String getReaderEmail() { return this.reader.getEmail(); }

    public Book getBook() { return book; }

    public LocalDate getDueDate() { return dueDate; }

    public Reader getReader() { return reader; }

    public void returnDate() { this.retunDate = LocalDate.now(); }

    public String getFullName() { return reader.getFirstName() + " " + reader.getlastName(); }

    public long getOverdueDays() {
        LocalDate today = LocalDate.now();
        if (today.isAfter(dueDate)) return ChronoUnit.DAYS.between(dueDate, today);
        return 0;
    }

    public double calculateFine() {
        long overdueDays = getOverdueDays();
        return overdueDays * 0.50;
    }

    @Override
    public String toString() {
        return "Loan: " +
                reader.getFirstName() +
                " " + reader.getlastName() +
                " borrowed " + book.getTitle() +
                " on " + loanDate +
                " due on " + dueDate;
    }

    public String getLoanDetails() {
        String details = "Loan id: " + loanId + "\n" +
                "   Reader id: " + this.reader.getId() + "\n" +
                "   Name: " + this.reader.getFirstName() + " " + this.reader.getlastName() + "\n" +
                "   Email: " + this.reader.getEmail() + "\n" +
                "   ISBN: " + book.getIsbn() + "\n" +
                "   Book: " + book.getTitle() + "\n" +
                "Loan date: " + loanDate + "\n" +
                "Due date: " + dueDate + "\n";
        if (retunDate != null) {
            details += "Return date: " + retunDate + "\n";
        }
        return details;
    }

    public void getLoans() {
        for (List<Loan> loans : loanMap.values()) {
            for (Loan loan : loans) {
                System.out.println(loan.getLoanDetails());
            }
        }
    }
}