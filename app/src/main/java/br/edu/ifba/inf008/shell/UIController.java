package br.edu.ifba.inf008.shell;

import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.interfaces.ICore;

import javafx.application.Application;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Side;

import static br.edu.ifba.inf008.shell.screen.BookScreen.showAddBookScreen;
import static br.edu.ifba.inf008.shell.screen.LoanScreen.showBorrowBookScreen;
import static br.edu.ifba.inf008.shell.screen.UserScreen.showAddUserScreen;
import static br.edu.ifba.inf008.shell.screen.FineScreen.showPayFineScreen;
import static br.edu.ifba.inf008.shell.screen.ReturnScreen.showReturnBookScreen;

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
        menuBar = new MenuBar();
        VBox vBox = new VBox(menuBar);
        Scene scene = new Scene(vBox, 500, 500);

        tabPane = new TabPane();
        tabPane.setSide(Side.BOTTOM);
        vBox.getChildren().addAll(tabPane);

        MenuItem addUserMenuItem = createMenuItem("User", "Add user");
        addUserMenuItem.setOnAction(e -> showAddUserScreen());

        MenuItem addBookMenuItem = createMenuItem("Book", "Add book");
        addBookMenuItem.setOnAction(e -> showAddBookScreen());

        MenuItem borrowBookMenuItem = createMenuItem("Loan", "Borrow book");
        borrowBookMenuItem.setOnAction(e -> showBorrowBookScreen());

        MenuItem returnBookMenuItem = createMenuItem("Return book", "Return book");
        returnBookMenuItem.setOnAction(e -> showReturnBookScreen());

        MenuItem payFineMenuItem = createMenuItem("Return book", "Pay fine");
        payFineMenuItem.setOnAction(e -> showPayFineScreen());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Library management system ISBN");
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
}


