//package br.edu.ifba.inf008.model;

//import java.util.ArrayList;
//import java.util.List;
//
//public class Reader extends User {
//    private List<Book> borrowedBooks;
//     /*O atributo borrowedBooks foi declarado como List<Book>
//    porque List é uma interface, o que permite flexibilidade para usar
//    diferentes implementações, como ArrayList, LinkedList, etc.*/
//
//    public Reader(String firstName, String lastName, String email) {
//        super(firstName, lastName, email);
//        this.borrowedBooks = new ArrayList<>();
//    }
//
//    @Override
//    public void display() {
//        super.display();
//    }
//
//    public List<Book> getBorrowedBooks() { return borrowedBooks; }
//    /*O método getBorrowedBooks() foi criado para permitir que a lista de livros do leitor seja populada*/
//    public boolean borrowBook(Book book) {
//        if (borrowedBooks.size() >= 5 || book.isBorrowed()) return false;
//        borrowedBooks.add(book);
//        book.borrow();
//        return true;
//    }
//
//    public boolean returnBook(Book book) {
//        if (borrowedBooks.remove(book)) {
//            book.returnBook();
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder bookList = new StringBuilder();
//        for (Book book : borrowedBooks) {
//            bookList.append("\n  - ").append(book.getTitle()).append(" by ").append(book.getAuthor()).append(" (ISBN: ").append(book.getIsbn()).append(")");
//        }
//        return this.getFirstName() + getlastName() + " (ID: " + getId() + ") - Books Borrowed: " + borrowedBooks.size() + (bookList.length() > 0 ? bookList.toString() : "");
//    }
//
//}

package br.edu.ifba.inf008.model;

import br.edu.ifba.inf008.persistence.DataPersistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reader extends User {
    private List<Loan> loans;

    public Reader(String firstName, String lastName, String email) {
        super(firstName, lastName, email);
        this.loans = new ArrayList<>();
    }

    @Override
    public void display() {
        super.display();
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public boolean borrowBook(Book book, LocalDate loanDate) {
//        if (loans.size() >= 5 || book.isBorrowed()) return false;
//        Loan loan = new Loan(this, book, loanDate);
//        loans.add(loan);
//        DataPersistence.addLoan(loan);
//        book.borrow();
//        return true;
        if (this.loans.size() < 5 && !this.loans.contains(book) && !book.isBorrowed()) {
            System.out.println("Teste de LOAN em Reader");
            System.out.println("Reader: " + this.getFirstName() + " " + this.getlastName());
            System.out.println("Email: " + this.getEmail());
            System.out.println("Book: " + book.getTitle() + " by " + book.getAuthor());
            System.out.println("Loan size: " + loans.size());

            Loan loan = new Loan(this, book, loanDate);
            book.borrow();
            this.loans.add(loan);
            DataPersistence.addLoan(loan);
            return true;
        }
        return false;
    }

    public boolean returnBook(Book book) {
        for (Loan loan : loans) {
            if (loan.getBook().equals(book)) {
                loans.remove(loan);
                book.returnBook();
                return true;
            }
        }
        return false;
    }

    public double calculateTotalFine() {
        double totalFine = 0;
        for (Loan loan : loans) {
            totalFine += loan.calculateFine();
        }
        return totalFine;
    }

    @Override
    public String toString() {
        StringBuilder loanList = new StringBuilder();
        for (Loan loan : loans) {
            loanList.append("\n  - ").append(loan.getBook().getTitle()).append(" by ").append(loan.getBook().getAuthor()).append(" (Due: ").append(loan.getDueDate()).append(")");
        }
        return this.getFirstName() + getlastName() + " (ID: " + getId() + ") - Books Borrowed: " + loans.size() + (loanList.length() > 0 ? loanList.toString() : "");
    }
}
