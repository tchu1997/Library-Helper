/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package library.helper.ui.booklist;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.helper.database.DatabaseHandler;
import library.helper.fxdialogs.FxAlerts;
import library.helper.ui.addbook.AddBookController;
import library.helper.ui.main.MainController;
import library.helper.ui.utility.LibraryHelperUtility;

/**
 * FXML Controller class
 *
 * @author kumaq
 */
public class Book_listController implements Initializable {

    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> idColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> publisherColumn;
    @FXML
    private TableColumn<Book, Boolean> availColumn;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Book> tableView;

    DatabaseHandler handler;
    ObservableList<Book> bookList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        handler = DatabaseHandler.getHandlerObject();
        loadData();
    }

    private void initCol() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        availColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));
    }

    private void loadData() {
        try {
            bookList.clear();
            String query = "SELECT * FROM " + handler.getBookTableName();
            ResultSet result = handler.execQuery(query);
            while (result.next()) {
                String title = result.getString("title");
                String id = result.getString("id");
                String author = result.getString("author");
                String publisher = result.getString("publisher");
                Boolean avail = result.getBoolean("isAvail");
                bookList.add(new Book(title, id, author, publisher, avail));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Book_listController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableView.setItems(bookList);

    }

    @FXML
    private void deleteBookSelection(ActionEvent event) {
        //Highlight the selected book's row
        Book selectedBook = tableView.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            FxAlerts.showError("No book selected", "Please select a book to delete.");
            return;
        }

        if (DatabaseHandler.getHandlerObject().isCheckedOut(selectedBook)) {
            FxAlerts.showError("Deletion Failed", "This book is checked out at the moment and can't be erased.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Book Deletion");
        confirmation.setContentText("Delete " + selectedBook.getTitle() + " ?");
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.get() == ButtonType.OK) {
            boolean succes = DatabaseHandler.getHandlerObject().removeBook(selectedBook);
            if (succes) {
                FxAlerts.showInformation("Book Deletion", selectedBook.getTitle() + "was erased from the library.");
                bookList.remove(selectedBook);
            } else {
                FxAlerts.showInformation("Deletion Failed", selectedBook.getTitle() + "was unable to erase from the library.");
            }
        } else {
            FxAlerts.showInformation("Delete Cancellation", "Deletion is cancelled.");
        }
    }

    @FXML
    private void editBookSelection(ActionEvent event) {
        Book selectedBook = tableView.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            FxAlerts.showError("No book selected", "Please select a book to edit.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/helper/ui/addbook/AddBook.fxml"));
            Parent parent = loader.load();

            AddBookController controller = (AddBookController) loader.getController();
            controller.getBookDataInEdit(selectedBook);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit");
            stage.setScene(new Scene(parent));
            stage.show();
            LibraryHelperUtility.setSceneIcon(stage);

            stage.setOnHiding((e) -> {
                refreshBookSelection(new ActionEvent());
            });

        } catch (IOException ex) {
            Logger.getLogger(Book_listController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void refreshBookSelection(ActionEvent event) {
        loadData();
    }

    @FXML
    private void addBook(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/helper/ui/addbook/AddBook.fxml"));
            Parent parent = loader.load();
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit");
            stage.setScene(new Scene(parent));
            stage.show();
            LibraryHelperUtility.setSceneIcon(stage);

            stage.setOnHiding((e) -> {
                refreshBookSelection(new ActionEvent());
            });

        } catch (IOException ex) {
            Logger.getLogger(Book_listController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
