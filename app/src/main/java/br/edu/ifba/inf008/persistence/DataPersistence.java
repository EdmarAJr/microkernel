package br.edu.ifba.inf008.persistence;

import br.edu.ifba.inf008.model.Loan;
import br.edu.ifba.inf008.model.Reader;
import br.edu.ifba.inf008.model.User;
import br.edu.ifba.inf008.model.Book;

import java.io.IOException;
import java.io.OutputStream;
import java.io.File;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class DataPersistence {

    public static Map<String, User> userMap = new HashMap<>();
    public static Map<String, Book> bookMap = new HashMap<>();
    public static Map<String, Loan> loanMap = new HashMap<>();

    private static FileInputStream fis;
    private static ObjectInputStream ois;
    private static FileOutputStream fos;
    private static ObjectOutputStream oos;
    private static final String FILE_PATH = "persistence.dat";

    // apagar depois dos testes
    static {
        addUser(new Reader("Admin", "admin", "admin@email.com"));
        addUser(new Reader("Leitor", "numero 1", "cliente@email.com"));
        addUser(new Reader("Edmar", "Amorim", "edmar@email.com"));
    }

    static {
        addBook(new Book ("Descubra seus pontos fortes 2.0", "Don Clifton", "Auto ajuda", 2019));
        addBook(new Book ("Descubra seus pontos fortes 4.0", "Don Clifton", "Auto ajuda", 2020));
        addBook(new Book ("Entendendo algoritmos", "Aditya Y. Bhargava", "Ciência da computação", 2017));
        addBook(new Book ("Estrutura de dados e algoritmos com JavaScript", "Loiane Groner", "Ciência da computação", 2018));
        addBook(new Book("Clean Code", "Robert C. Martin", "Software Development", 2008));
        addBook(new Book("The Pragmatic Programmer", "Andrew Hunt", "Software Development", 1999));
        addBook(new Book("Design Patterns", "Erich Gamma", "Software Development", 1994));
        addBook(new Book("Refactoring", "Martin Fowler", "Software Development", 1999));
        addBook(new Book("Effective Java", "Joshua Bloch", "Software Development", 2001));
    }

//    static {
//        addLoan(new Loan("admin@email", "Descubra seus pontos fortes 2.0", "2021-06-01", "2021-06-30"));
//    }


    public static void load () throws IOException, ClassNotFoundException {
        File file = new File(FILE_PATH);
        File parentDirectory = file.getParentFile();
        if (parentDirectory != null && !parentDirectory.exists()) {
            if (parentDirectory.mkdirs()) {
                System.out.println("DIRECTORIES CREATE: " + parentDirectory.getAbsolutePath());
            } else {
                throw new IOException("UNABLE TO CREATE DIRECTORIES: " + parentDirectory.getAbsolutePath());
            }
        }

        if (!file.exists()) {
            System.out.println("FILE " + FILE_PATH + " NOT FOUND. CREATING NEW FILE.");
            return;
        }

        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);

            Object obj;
            try {
                while (true) {
                    obj = ois.readObject();

                    if (obj instanceof User) {
                        DataPersistence.userMap.put(((User) obj).getEmail(), (User) obj);
                    } else if (obj instanceof Book) {
                        DataPersistence.bookMap.put(((Book) obj).getTitle(), (Book) obj);
                    } else if (obj instanceof Loan) {
                        System.out.println("Teste de LOAN Salvar");
                        DataPersistence.loanMap.put(((Loan) obj).getReaderEmail(), (Loan) obj);
                    }
                }
            } catch (EOFException ex) {}
        } finally {
            if (fis != null) fis.close();
            if (ois != null) ois.close();
        }



    }

    public static void save() throws Exception {
        File file = new File(FILE_PATH);
        File parentDirectory = file.getParentFile();
        if (parentDirectory != null && !parentDirectory.exists()) {
            if (parentDirectory.mkdirs()) {
                System.out.println("DIRECTORIES CREATE: " + parentDirectory.getAbsolutePath());
            } else {
                throw new IOException("UNABLE TO CREATE DIRECTORIES: " + parentDirectory.getAbsolutePath());
            }
        }

        try {
            fos = new FileOutputStream(FILE_PATH, false);
            oos = new ObjectOutputStream(fos);

            //oos.writeObject("(START)");

//            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
//                oos.writeObject(userMap);
//                oos.writeObject(bookMap);
//                oos.writeObject(loanMap);
//            }

            for (User user : userMap.values()) {
                //oos.writeObject(user);
                oos.writeObject(userMap);
            }
            for (Book book : bookMap.values()) {
                oos.writeObject(book);
                //oos.writeObject(bookMap);
            }
            for (Loan loan : loanMap.values()) {
                //oos.writeObject(loan);
                oos.writeObject(loanMap);
            }

            //oos.writeObject("(FINISH)");
            System.out.println("DATA SAVED SUCCESSFULLY!");
        } finally {
            if (fos != null) fos.close();
            if (oos != null) oos.close();
        }
    }

//    public static void loadUsers() throws IOException, ClassNotFoundException {
//        File fileName = new File(FILE_PATH);
//        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
//            userMap = (Map<String, User>) ois.readObject();
//        }
//    }

//    public static void loadBooks() throws IOException, ClassNotFoundException {
//        File fileName = new File(FILE_PATH);
//        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
//            bookMap = (Map<String, Book>) ois.readObject();
//        }
//    }
//
//    public static void loadLoans() throws IOException, ClassNotFoundException {
//        File fileName = new File(FILE_PATH);
//        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
//            loanMap = (Map<String, Loan>) ois.readObject();
//        }
//    }
//
//
//    public static void saveUsers() throws IOException {
//        File fileName = new File(FILE_PATH);
//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
//            oos.writeObject(userMap);
//        }
//    }

//    public static void saveBooks() throws IOException {
//        File fileName = new File(FILE_PATH);
//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
//            oos.writeObject(bookMap);
//        }
//    }

//    public static void saveLoans() throws IOException {
//        File fileName = new File(FILE_PATH);
//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
//            oos.writeObject(loanMap);
//        }
//    }

    public static Map<String, User> getUserMap() { return userMap;}

    public static Map<String, Book> getBookMap() { return bookMap; }

    public static Map<String, Loan> getLoanMap() { return loanMap; }

    public static void addUser(User user) {
        if (userMap.containsKey(user.getEmail())) {
            System.out.println(user.getUserDetails());
            userMap.put(user.getEmail(), user);
        } else {
            System.out.println("User with the same email already exists.");
        }
    }

    public static void addBook(Book book) {
        if (bookMap.containsKey(book.getTitle())) {
            System.out.println(book.getBookDetails());
            bookMap.put(book.getTitle(), book);
        } else {
            System.out.println("Book with the same title already exists.");
        }
    }

    public static void addLoan(Loan loan) {
        System.out.println("Teste de LOAN em persistence addLoan");
        System.out.println(loan.getLoanDetails());
        loanMap.put(loan.getReaderEmail(), loan);
    }
}