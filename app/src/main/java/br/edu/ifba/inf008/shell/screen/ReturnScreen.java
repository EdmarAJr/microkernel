package br.edu.ifba.inf008.shell.screen;

import br.edu.ifba.inf008.model.Book;
import br.edu.ifba.inf008.model.Fine;
import br.edu.ifba.inf008.model.Loan;
import br.edu.ifba.inf008.model.Reader;
import br.edu.ifba.inf008.persistence.DataPersistence;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class ReturnScreen {
    private TabPane tabPane;

    public ReturnScreen(TabPane tabPane) { this.tabPane = tabPane; }

    public static void showReturnBookScreen() {
        Stage returnStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setSpacing(10);

        TextField readerEmailField = new TextField();
        ComboBox<String> bookTitleField = new ComboBox<>();
        bookTitleField.setEditable(true);

        readerEmailField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.isEmpty()) {
                Reader reader = (Reader) DataPersistence.getUserMap().get(newText);
                if (reader != null) {
                    List<String> bookTitlesReader = reader.getLoans().stream()
                            .map(Loan::getBook)
                            .map(Book::getTitle)
                            .collect(Collectors.toList());

                    bookTitleField.getItems();
                    bookTitleField.getItems().addAll(bookTitlesReader);
                } else {
                    bookTitleField.getItems().clear();
                }
            } else {
                bookTitleField.getItems().clear();
            }
        });

        Button returnBtn = new Button("Return book");
        HBox buttonBox = new HBox(returnBtn);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);

        returnBtn.setOnAction(e -> {
            String readerEmail = readerEmailField.getText();
            String bookTitle = bookTitleField.getEditor().getText();

            if (readerEmail.isEmpty() || bookTitle.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("All fields are required!");
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

            Book book = DataPersistence.getBookMap().get(bookTitle);
            if (book == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Book not found!");
                alert.showAndWait();
                return;
            }

            Loan loan = reader.getLoans().stream()
                    .filter(l -> l.getBook().equals(book))
                    .findFirst()
                    .orElse(null);

            if (loan == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Loan not found!");
                alert.showAndWait();
                return;
            }

            LocalDate returnDate = LocalDate.now();
            long daysLate = ChronoUnit.DAYS.between(loan.getDueDate(), returnDate);
            double fineAmount = 0.0;

            if (daysLate > 0) {
                fineAmount = daysLate * 0.5;
                Fine fine = new Fine(reader, fineAmount);
                reader.addFine(fine);
            }

            if (reader.returnBook(book) && fineAmount == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Book returned successfully!");
                alert.showAndWait();
                loan.returnDate();
                bookTitleField.getEditor().clear();
            } else if (fineAmount > 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Book returned successfully, but a fine of $" + fineAmount + " has been added to the reader's account.");
                alert.showAndWait();
                readerEmailField.clear();
                bookTitleField.getEditor().clear();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Cannot return book. The book might not be borrowed by this reader.");
                alert.showAndWait();
            }
        });

        layout.getChildren().addAll(new Label("User email"), readerEmailField, new Label("Book title"), bookTitleField, returnBtn);
        Scene scene = new Scene(layout, 300, 300);
        returnStage.setScene(scene);
        returnStage.setTitle("Return book");
        returnStage.show();
    }

}