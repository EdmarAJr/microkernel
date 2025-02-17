package br.edu.ifba.inf008.model;

public class Fine {
    private Reader reader;
    private double amount;

    public Fine(Reader reader, double amount) {
        this.reader = reader;
        this.amount = amount;
    }

    public User getUser() { return reader; }
    public double getAmount() { return amount; }

    @Override
    public String toString() {
        return "Fine: " + reader.getFirstName() + reader.getlastName() + " owes $" + amount;
    }
}
