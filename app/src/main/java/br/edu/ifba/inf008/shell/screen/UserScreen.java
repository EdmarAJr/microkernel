package br.edu.ifba.inf008.shell.screen;

import br.edu.ifba.inf008.model.Reader;
import br.edu.ifba.inf008.persistence.DataPersistence;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserScreen {
    public static void showAddUserScreen() {
        Stage userStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setSpacing(10);

        TextField userFirstNameField = new TextField();
        TextField userLastNameField = new TextField();
        TextField userEmailField = new TextField();

        Button saveBtn = new Button("Save user");
        HBox buttonBox = new HBox(saveBtn);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);

        saveBtn.setOnAction(e -> {
            String userFirstName = userFirstNameField.getText();
            String userLastName = userLastNameField.getText();
            String userEmail = userEmailField.getText();

            if (userFirstName.isEmpty() || userLastName.isEmpty() || userEmail.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("All fields are required!");
                alert.showAndWait();
                return;
            }

            if (!userEmail.contains("@") || !userEmail.contains(".")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Invalid email!");
                alert.showAndWait();
                return;
            }

            if (DataPersistence.getUserMap().containsKey(userEmail)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("User already exists!");
                alert.showAndWait();
            } else {
                DataPersistence.addUser(new Reader(userFirstName, userLastName, userEmail));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("User added successfully!");
                alert.showAndWait();

                userFirstNameField.clear();
                userLastNameField.clear();
                userEmailField.clear();
            }
        });

        layout.getChildren().addAll(new Label("First name"), userFirstNameField, new Label("Last name"), userLastNameField, new Label("Email"), userEmailField, saveBtn);
        Scene scene = new Scene(layout, 300, 300);
        userStage.setScene(scene);
        userStage.setTitle("Add user");
        userStage.show();
    }
}
