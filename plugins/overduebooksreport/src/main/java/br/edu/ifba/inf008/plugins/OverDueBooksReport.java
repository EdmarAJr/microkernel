package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.model.Loan;
import br.edu.ifba.inf008.persistence.DataPersistence;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;

import javafx.scene.control.MenuItem;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Insets;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class OverDueBooksReport implements IPlugin {

    @Override
    public boolean init() {
        IUIController uiController = ICore.getInstance().getUIController();

        MenuItem menuItem = uiController.createMenuItem("Overdue books", "Overdue books report");
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                showOverReportScreen();
            }
        });

        return true;
    }

    private void showOverReportScreen() {
        Stage reportStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setSpacing(10);

        String report = generateReport();

        TextArea reportArea = new TextArea(report);
        reportArea.setPrefWidth(400);
        reportArea.setPrefHeight(300);
        reportArea.setEditable(false);

        layout.getChildren().addAll(new Label("Overdue books and fines:"), reportArea);
        Scene scene = new Scene(layout, 400, 400);
        reportStage.setScene(scene);
        reportStage.setTitle("Overdue books report");
        reportStage.show();
    }

    public String generateReport() {
        StringBuilder report = new StringBuilder("\n");
        LocalDate today = LocalDate.now();

        for (List<Loan> loans : DataPersistence.getLoanMap().values()) {
            for (Loan loan : loans) {
                LocalDate dueDate = loan.getDueDate();
                if (dueDate.isBefore(today)) {
                    long daysLate = ChronoUnit.DAYS.between(dueDate, today);
                    double fineAmount = daysLate * 0.5;
                    report.append("Book: ").append(loan.getBook().getTitle()).append("\n").append("\t")
                            .append("Full name: ").append(loan.getFullName()).append("\n").append("\t")
                            .append("Email reader: ").append(loan.getReader().getEmail()).append("\n").append("\t")
                            .append("Days late: ").append(daysLate).append("\n").append("\t")
                            .append("Fine: $").append(fineAmount).append("\n\n");
                }
            }
        }
        return report.toString();
    }
}