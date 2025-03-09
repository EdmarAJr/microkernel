package br.edu.ifba.inf008.shell.screen;

import br.edu.ifba.inf008.model.Book;
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

public class BookScreen {
    private TabPane tabPane;

    public BookScreen(TabPane tabPane) { this.tabPane = tabPane; }

    public static void showAddBookScreen() {
        Stage bookStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setSpacing(10);

        TextField titleField = new TextField();
        TextField authorField = new TextField();
        TextField genreField = new TextField();
        TextField yearField = new TextField();

        Button saveBtn = new Button("Save book");
        HBox buttonBox = new HBox(saveBtn);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);

        saveBtn.setOnAction(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            String genre = genreField.getText();
            String yearText = yearField.getText();

            if (title.isEmpty() || author.isEmpty() || genre.isEmpty() || yearText.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("All fields are required!");
                alert.showAndWait();
                return;
            }

            int year;
            try {
                year = Integer.parseInt(yearText);
                if (year <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Year must be a positive number!");
                alert.showAndWait();
                return;
            }

            if (DataPersistence.getBookMap().containsKey(title)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Book already exists!");
                alert.showAndWait();
            } else {
                DataPersistence.addBook(new Book(title, author, genre, year));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Book added successfully!");
                alert.showAndWait();
                titleField.clear();
                authorField.clear();
                genreField.clear();
                yearField.clear();
            }
        });

        layout.getChildren().addAll(new Label("Title"), titleField, new Label("Author"), authorField, new Label("Genre"), genreField, new Label("Year"), yearField, saveBtn);
        Scene scene = new Scene(layout, 300, 400);
        bookStage.setScene(scene);
        bookStage.setTitle("Add book");
        bookStage.show();
    }
}