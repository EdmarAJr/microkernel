//package br.edu.ifba.inf008.model;
//
//public class Loan {
//    private int loanId;
//    private int readerId;
//    private int bookId;
//    private String loanDate;
//    private String dueDate;
//
//    public Loan(int loanId, int readerId, int bookId, String loanDate, String dueDate) {
//        this.loanId = loanId;
//        this.readerId = readerId;
//        this.bookId = bookId;
//        this.loanDate = loanDate;
//        this.dueDate = dueDate;
//    }
//
//    public int getLoanId() {
//        return loanId;
//    }
//
//    public int getReaderId() {
//        return readerId;
//    }
//
//    public int getBookId() {
//        return bookId;
//    }
//
////    public String getLoanDate() {
////        return loanDate;
////    }
////
////    public String getDevolutionDate() {
////        return dueDate;
////    }
////
////    public void setDevolutionDate(String devolutionDate) {
////        this.dueDate = dueDate;
////    }
//
//    @Override
//    public String toString() {
//        return "Loan ID: " + loanId + " - Reader ID: " + readerId + " - Book ID: " + bookId + " - Loan Date: " + loanDate + " - Devolution Date: " + dueDate;
//    }
//
////    @Override
////    public String toString() {
////        return "Loan: " + book.getTitle() + " to " + user.getName() + " (Due: " + dueDate + ")";
////    }
//}

package br.edu.ifba.inf008.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static br.edu.ifba.inf008.persistence.DataPersistence.bookMap;
import static br.edu.ifba.inf008.persistence.DataPersistence.loanMap;

public class Loan implements Serializable {
    private int loanId;
    private static int loanIdIncrement = 0;
    private Reader reader;
    private Book book;
    private LocalDate loanDate;
    private LocalDate dueDate;

    public Loan(Reader reader, Book book, LocalDate loanDate) {
        try {
            if (reader == null || book == null || loanDate == null)
                throw new IllegalArgumentException("READER, BOOK AND LOAN DATE CANNOT BE EMPTY FIELDS.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        /*esta validação estava atrapalhando*/
//&& (loanMap.putIfAbsent(reader.getEmail().trim(), this) == null)
        if (reader != null && book != null && loanDate != null) {
            this.loanId = ++loanIdIncrement;
            this.reader = reader;
            this.book = book;
            this.loanDate = loanDate;
            this.dueDate = loanDate.plusDays(14);
            if (bookMap.get(book.getTitle()) != null) {
                if (bookMap.get(book.getTitle()).isBorrowed()) System.out.println("BOOK ALREADY BORROWED!");
                else bookMap.get(book.getTitle()).borrow();
            } else System.out.println("BOOK NOT REGISTERED!");
        } else {
            System.out.println("LOAN FAILURE!");
        }
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
        return details;
    }

    public String getReaderEmail() {
        System.out.println("Teste de READER EMAIL em Loan");
        System.out.println("Reader email: " + this.reader.getEmail());
        return this.reader.getEmail();
    }

    public Book getBook() { return book; }

    public LocalDate getLoanDate() { return loanDate; }

    public LocalDate getDueDate() { return dueDate; }

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
        return "Loan: " + reader.getFirstName() + " " + reader.getlastName() + " borrowed " + book.getTitle() + " on " + loanDate + " due on " + dueDate;
    }

    public int getLoanId() { return loanId;   }

}