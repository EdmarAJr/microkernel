package br.edu.ifba.inf008.shell;

import br.edu.ifba.inf008.model.Book;
import br.edu.ifba.inf008.model.Fine;
import br.edu.ifba.inf008.model.Loan;

import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.interfaces.ICore;

import br.edu.ifba.inf008.model.Reader;
import br.edu.ifba.inf008.persistence.DataPersistence;

import javafx.application.Application;
import javafx.geometry.Pos;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

import javafx.scene.Scene;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.geometry.Insets;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import br.edu.ifba.inf008.shell.PluginController;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.application.Platform;
import javafx.collections.ObservableList;


public class UIController extends Application implements IUIController
{
    private ICore core;
    private MenuBar menuBar;
    private TabPane tabPane;
    private static UIController uiController;

    public UIController() {
    }

    @Override
    public void init() {
        uiController = this;
    }

    public static UIController getInstance() {
        return uiController;
    }

    @Override
    public void start(Stage primaryStage) {

        /*cria um espaço vertical entre os itens*/
       //VBox root = new VBox(10);
       // Scene scene = new Scene(root, 600, 600);

        menuBar = new MenuBar(); //Inicializa um novo objeto MenuBar.
        VBox vBox = new VBox(menuBar); //Cria um novo layout VBox e adiciona a MenuBar a ele.
        Scene scene = new Scene(vBox, 500, 500); // Cria uma nova Scene com o VBox como seu nó raiz e define as dimensões da cena para 960x600 pixels.

        tabPane = new TabPane(); //Inicializa um novo objeto TabPane.
        tabPane.setSide(Side.BOTTOM); //Define a posição das abas na parte inferior do TabPane.
        vBox.getChildren().addAll(tabPane); //Adiciona o TabPane à lista de filhos do layout VBox, para que ele seja exibido na interface do

        MenuItem addUserMenuItem = createMenuItem("User", "Add User");
        addUserMenuItem.setOnAction(e -> showAddUserScreen());

        MenuItem addBookMenuItem = createMenuItem("Book", "Add Book");
        addBookMenuItem.setOnAction(e -> showAddBookScreen());

        MenuItem borrowBookMenuItem = createMenuItem("Loan", "Borrow Book");
        borrowBookMenuItem.setOnAction(e -> showBorrowBookScreen());

        MenuItem returnBookMenuItem = createMenuItem("Return Book", "Return Book");
        returnBookMenuItem.setOnAction(e -> showReturnBookScreen());

        MenuItem payFineMenuItem = createMenuItem("Return Book", "Pay Fine");
        payFineMenuItem.setOnAction(e -> showPayFineScreen());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Library Management System ISBN");
        primaryStage.show();

        Core.getInstance().getPluginController().init();
    }

    public MenuItem createMenuItem(String menuText, String menuItemText) {
        // Criar o menu caso ele nao exista
        Menu newMenu = null;
        for (Menu menu : menuBar.getMenus()) {
            if (menu.getText() == menuText) {
                newMenu = menu;
                break;
            }
        }
        if (newMenu == null) {
            newMenu = new Menu(menuText);
            menuBar.getMenus().add(newMenu);
        }

        // Criar o menu item neste menu
        MenuItem menuItem = new MenuItem(menuItemText);
        newMenu.getItems().add(menuItem);

        return menuItem;
    }

    public boolean createTab(String tabText, Node contents) {
        Tab tab = new Tab();
        tab.setText(tabText);
        tab.setContent(contents);
        tabPane.getTabs().add(tab);

        return true;
    }

    private void showAddUserScreen() {
        Stage userStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setSpacing(10);

        TextField userFirstNameField = new TextField();
        TextField userLastNameField = new TextField();
        TextField userEmailField = new TextField();

        Button saveBtn = new Button("Save User");
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

    private void showAddBookScreen() {
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

    private void showBorrowBookScreen() {
        Stage loanStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setSpacing(10);

        TextField readerEmailField = new TextField();
        DatePicker loanDatePicker = new DatePicker();
        Label dueDateLabel = new Label("Due date: ");

        List<String> bookTitles = DataPersistence.getBookMap().keySet().stream().collect(Collectors.toList());

        ComboBox<String> bookTitleField = new ComboBox<>();
        bookTitleField.setEditable(true);

        bookTitleField.getItems().addAll(bookTitles);
        bookTitleField.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            List<String> filteredTitles = bookTitles.stream()
                    .filter(title -> title.toLowerCase().contains(newText.toLowerCase()))
                    .collect(Collectors.toList());

            if (!filteredTitles.isEmpty()) {
                bookTitleField.getItems().clear();
                bookTitleField.getItems().addAll(filteredTitles);
                bookTitleField.show();
            } else {
                bookTitleField.hide();
            }
        });

        loanDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                LocalDate dueDate = newDate.plusDays(14); // Assuming a 14-day loan period
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

            if (reader.borrowBook(book, loanDate)) {
                LocalDate dueDate = loanDate.plusDays(14);
                dueDateLabel.setText("Due Date: " + dueDate.toString());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Book borrowed successfully!");
                alert.showAndWait();
                //readerEmailField.clear();
                bookTitleField.getEditor().clear();
                //loanDatePicker.setValue(null);
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


    private void showReturnBookScreen() {
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

                    bookTitleField.getItems().clear();
                    bookTitleField.getItems().addAll(bookTitlesReader);
                } else {
                    bookTitleField.getItems().clear();
                }
            } else {
                bookTitleField.getItems().clear();
            }
        });

        Button returnBtn = new Button("Return Book");
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
                System.out.println("Fine amount: " + fineAmount);
                Fine fine = new Fine(reader, fineAmount);
                reader.addFine(fine); // Add fine to the reader's account
            }

            if (reader.returnBook(book) && fineAmount == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Book returned successfully!");
                alert.showAndWait();
                readerEmailField.clear();
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

    private void showPayFineScreen() {
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

        layout.getChildren().addAll(new Label("Reader Email"), readerEmailField, fineAmountLabel, payFineBtn);
        Scene scene = new Scene(layout, 300, 200);
        fineStage.setScene(scene);
        fineStage.setTitle("Pay fine");
        fineStage.show();
    }

//    @Override
//    public void close() {
//        Platform.exit();
//    }
}
