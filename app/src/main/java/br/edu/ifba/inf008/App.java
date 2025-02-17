package br.edu.ifba.inf008;

import br.edu.ifba.inf008.shell.Core;
import br.edu.ifba.inf008.persistence.DataPersistence;

import java.io.IOException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {
//        DataPersistence.loadUsers();
//        DataPersistence.loadBooks();
//        DataPersistence.loadLoans();
        DataPersistence.load();
        Core.init();
        DataPersistence.save();
//        DataPersistence.saveUsers();
//        DataPersistence.saveBooks();
//        DataPersistence.saveLoans();
    }
}
