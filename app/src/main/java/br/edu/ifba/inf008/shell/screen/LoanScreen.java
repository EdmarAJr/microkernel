package br.edu.ifba.inf008.shell.screen;

import br.edu.ifba.inf008.model.Book;
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
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class LoanScreen {
    private TabPane tabPane;

    public LoanScreen(TabPane tabPane) { this.tabPane = tabPane; }

    public static void showBorrowBookScreen() {
        Stage loanStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setSpacing(10);

        TextField readerEmailField = new TextField();
        DatePicker loanDatePicker = new DatePicker();
        Label dueDateLabel = new Label("Due date: ");

        List<String> bookTitles = DataPersistence.getBookMap().values().stream()
                .filter(book -> !book.isBorrowed())
                .map(Book::getTitle)
                .collect(Collectors.toList());

        ComboBox<String> bookTitleField = new ComboBox<>();
        bookTitleField.setEditable(true);

        bookTitleField.getItems().addAll(bookTitles);
        bookTitleField.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            List<String> filteredTitles = bookTitles.stream()
                    .filter(title -> title.toLowerCase().contains(newText.toLowerCase()))
                    .collect(Collectors.toList());

            if (!filteredTitles.isEmpty()) {
                bookTitleField.show();
            } else {
                bookTitleField.hide();
            }
        });

        loanDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                LocalDate dueDate = newDate.plusDays(14);
                dueDateLabel.setText("Due date: " + dueDate.toString());
            } else {
                dueDateLabel.setText("Due date: ");
            }
        });

        Button borrowBtn = new Button("Borrow book");
        HBox buttonBox = new HBox(borrowBtn);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);

        borrowBtn.setOnAction(e -> {
            String readerEmail = readerEmailField.getText();
            String bookTitle = bookTitleField.getEditor().getText();
            LocalDate loanDate = loanDatePicker.getValue();

            if (readerEmail.isEmpty() || bookTitle.isEmpty() || loanDate == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("All fields are required!");
                alert.showAndWait();
                return;
            }

            Reader reader = (Reader) DataPersistence.getUserMap().get(readerEmail);
            Book book = DataPersistence.getBookMap().get(bookTitle);

            if (reader == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Reader not found!");
                alert.showAndWait();
                return;
            }

            if (book == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Book not found!");
                alert.showAndWait();
                return;
            }

            if(!reader.getFines().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Reader has fines to pay!");
                alert.showAndWait();
                return;
            }

            boolean hasOverdueBooks = reader.getLoans().stream()
                    .anyMatch(loan -> loan.getDueDate().isBefore(LocalDate.now()));

            if (hasOverdueBooks) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Reader has overdue books!");
                alert.showAndWait();
                return;
            }

            if (reader.borrowBook(book, loanDate)) {
                LocalDate dueDate = loanDate.plusDays(14);
                dueDateLabel.setText("Due date: " + dueDate.toString());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Book borrowed successfully!");
                alert.showAndWait();
                bookTitleField.getEditor().clear();

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Cannot borrow book. Either the book is already borrowed or the reader has reached the limit.");
                alert.showAndWait();
            }
        });

        layout.getChildren().addAll(new Label("User email"), readerEmailField, new Label("Book title"), bookTitleField, new Label("Loan date"), loanDatePicker, dueDateLabel, borrowBtn);
        Scene scene = new Scene(layout, 300, 300);
        loanStage.setScene(scene);
        loanStage.setTitle("Borrow book");
        loanStage.show();
    }
}