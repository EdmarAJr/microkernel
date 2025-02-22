package br.edu.ifba.inf008;

import br.edu.ifba.inf008.shell.Core;
import br.edu.ifba.inf008.persistence.DataPersistence;

public class App {
    public static void main(String[] args) throws Exception {
        DataPersistence.load();
        Core.init();
        DataPersistence.save();
    }
}
