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

import java.util.Comparator;
import java.util.List;

public class BorrowedBooksReport implements IPlugin {

    @Override
    public boolean init() {
        IUIController uiController = ICore.getInstance().getUIController();

        MenuItem menuItem = uiController.createMenuItem("Borrowed books", "Borrowed books report");
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                showReportScreen();
            }
        });

        return true;
    }

    private void showReportScreen() {
        Stage reportStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setSpacing(10);

        String report = generateReport();

        TextArea reportArea = new TextArea(report);
        reportArea.setPrefWidth(400);
        reportArea.setPrefHeight(300);
        reportArea.setEditable(false);

        layout.getChildren().addAll(new Label("All borrowed books:"), reportArea);
        Scene scene = new Scene(layout, 400, 400);
        reportStage.setScene(scene);
        reportStage.setTitle("Borrowed books report");
        reportStage.show();
    }

    public String generateReport() {
        StringBuilder report = new StringBuilder("\n");
        DataPersistence.getLoanMap().values().stream()
                .flatMap(List::stream)
                .sorted(Comparator.comparing(Loan::getLoanId))
                .forEach(loan -> report.append(loan.getLoanDetails()).append("\n"));
        return report.toString();
    }
}
