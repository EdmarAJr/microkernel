package br.edu.ifba.inf008.shell.screen;

import br.edu.ifba.inf008.model.Fine;
import br.edu.ifba.inf008.model.Reader;
import br.edu.ifba.inf008.persistence.DataPersistence;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FineScreen {
    private TabPane tabPane;

    public FineScreen(TabPane tabPane) { this.tabPane = tabPane; }

    public static void showPayFineScreen() {
        Stage fineStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setSpacing(10);

        TextField readerEmailField = new TextField();
        Label fineAmountLabel = new Label("Fine amount: $0.0");

        readerEmailField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.isEmpty()) {
                Reader reader = (Reader) DataPersistence.getUserMap().get(newText);
                if (reader != null) {
                    double totalFine = reader.getFines().stream().mapToDouble(Fine::getAmount).sum();
                    fineAmountLabel.setText("Fine amount: $" + totalFine);
                } else {
                    fineAmountLabel.setText("Fine amount: $0.0");
                }
            } else {
                fineAmountLabel.setText("Fine amount: $0.0");
            }
        });

        Button payFineBtn = new Button("Pay fine");
        HBox buttonBox = new HBox(payFineBtn);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);

        payFineBtn.setOnAction(e -> {
            String readerEmail = readerEmailField.getText();

            if (readerEmail.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Reader email is required!");
                alert.showAndWait();
                return;
            }

            Reader reader = (Reader) DataPersistence.getUserMap().get(readerEmail);
            if (reader == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Reader not found!");
                alert.showAndWait();
                return;
            }

            reader.payFines();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Fines paid successfully!");
            alert.showAndWait();
            readerEmailField.clear();
            fineAmountLabel.setText("Fine amount: $0.0");
        });

        layout.getChildren().addAll(new Label("Reader email"), readerEmailField, fineAmountLabel, payFineBtn);
        Scene scene = new Scene(layout, 300, 200);
        fineStage.setScene(scene);
        fineStage.setTitle("Pay fine");
        fineStage.show();
    }
}