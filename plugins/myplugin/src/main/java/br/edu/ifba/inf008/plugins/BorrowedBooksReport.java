package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.IReportPlugin;
import br.edu.ifba.inf008.model.Loan;
import br.edu.ifba.inf008.persistence.DataPersistence;

import java.util.Map;

public class BorrowedBooksReport implements IReportPlugin {
    @Override
    public String generateReport() {
        StringBuilder report = new StringBuilder("Borrowed Books Report:\n");
//        for (Loan loan : DataPersistence.getLoanMap()) {
//            report.append(loan.toString()).append("\n");
//        }
//        return report.toString();

        for (Map.Entry<String, Loan> entry : DataPersistence.getLoanMap().entrySet()) {
            Loan loan = entry.getValue();
            report.append(loan.toString()).append("\n");
        }
        return report.toString();
    }
}