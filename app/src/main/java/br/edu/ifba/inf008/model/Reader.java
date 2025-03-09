package br.edu.ifba.inf008.model;
import br.edu.ifba.inf008.persistence.DataPersistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reader extends User {
    private List<Loan> loans;
    private List<Fine> fines;

    public Reader(String firstName, String lastName, String email) {
        super(firstName, lastName, email);
        this.loans = new ArrayList<>();
        this.fines = new ArrayList<>();
    }

    @Override
    public void display() { super.display();   }

    public void payFines() { this.fines.clear(); }

    public List<Fine> getFines() { return fines; }

    public void addFine(Fine fine) { this.fines.add(fine); }

    public List<Loan> getLoans() { return loans; }

    public boolean borrowBook(Book book, LocalDate loanDate) {
        if (this.loans.size() < 5 && !this.loans.contains(book) && !book.isBorrowed()) {
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

    @Override
    public String toString() {
        StringBuilder loanList = new StringBuilder();
        for (Loan loan : loans) {
            loanList.append("\n  - ").append(loan.getBook().getTitle()).append(" by ").append(loan.getBook().getAuthor()).append(" (Due: ").append(loan.getDueDate()).append(")");
        }
        return this.getFirstName() + getlastName() + " (ID: " + getId() + ") - Books Borrowed: " + loans.size() + (loanList.length() > 0 ? loanList.toString() : "");
    }

    public double calculateTotalFine() {
        double totalFine = 0;
        for (Loan loan : loans) {
            totalFine += loan.calculateFine();
        }
        return totalFine;
    }
}
