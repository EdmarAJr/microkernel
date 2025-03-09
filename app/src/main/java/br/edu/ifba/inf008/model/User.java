package br.edu.ifba.inf008.model;
import static br.edu.ifba.inf008.persistence.DataPersistence.userMap;

import java.io.Serializable;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public abstract class User implements Serializable {
    private static int idIncrement = 0;
    private int id;
    private String firstName;
    private String lastName;
    private String email;

    public User(String firstName, String lastName, String email) {
        try {
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty())
                throw new IllegalArgumentException("NAME AND EMAIL CANNOT BE EMPTY FIELDS.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        if(userMap.putIfAbsent(email.trim(), this) == null){
            this.id = ++idIncrement;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        } else {
            System.out.println("EMAIL ALREADY REGISTERED!");
        }
    }

    public int getId() { return id; }

    public String getFirstName() { return firstName; }

    public String getlastName() { return lastName; }

    public String getEmail() { return email; }

    protected void display() {
        System.out.println("name: " + this.firstName + " " + this.lastName);
        System.out.println("email: " + this.email);
    }

    public String getUserDetails() {
        String details = "Id: " + this.id + "\n" +
                "First name: " + this.firstName + "\n" +
                "Last name: " + this.lastName + "\n" +
                "Email: " + this.email + "\n";
        return details;
    }
}