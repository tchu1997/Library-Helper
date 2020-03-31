/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.helper.ui.addbook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import library.helper.database.DatabaseHandler;
import library.helper.fxdialogs.FxAlerts;
import library.helper.ui.booklist.Book;

/**
 *
 * @author kumaq
 */
public class AddBookController implements Initializable {

    private Label label;
    @FXML
    private JFXTextField title;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField author;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXTextField publisher;
    @FXML
    private Text titleError;
    @FXML
    private Text idError;
    @FXML
    private Text authorError;
    @FXML
    private Text publisherError;

    DatabaseHandler handler;
    @FXML
    private AnchorPane rootPane;
    private Boolean isEditing = Boolean.FALSE;

    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handler = DatabaseHandler.getHandlerObject();     
    }

    @FXML
    // add a book in the database
    private void addBook(ActionEvent event) {
        // 1. get book's information and check if this book exists in the database or not
        String bookTitle = title.getText();
        String bookID = id.getText();
        String bookAuthor = author.getText();
        String bookPublisher = publisher.getText();

        if (isEditing) {
            editBook();
            return;
        }
        // 2. if book's info is valid, add the book to database
        if (isBookInfoValid(bookTitle, bookID, bookAuthor, bookPublisher) && !isBookAlreadyAdded(bookID)) {
            String INSERT = "INSERT INTO " + handler.getBookTableName() + " VALUES ("
                    + "'" + bookID + "',"
                    + "'" + bookTitle + "',"
                    + "'" + bookAuthor + "',"
                    + "'" + bookPublisher + "',"
                    + "'" + true + "'"
                    + ")";
            if (handler.execAction(INSERT)) {
                FxAlerts.showError("Confirmation", "Book is successfully added.");
    
                // close add-book gui after book is successfully added
                Stage stage = (Stage) rootPane.getScene().getWindow();
                stage.close();
            } else {
                FxAlerts.showError("Failed", "An error occurred.\nThis book cannot be added.");
            }
        }

    }

    @FXML
    // close add-book gui when user clisks "cancel" button
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    // check if a book's information is valid
    // if it is not, display appropriate message
    public boolean isBookInfoValid(String title, String id, String author, String publisher) {
        String[] info = {title, id, author, publisher};
        Text[] texts = {titleError, idError, authorError, publisherError};
        boolean isValid = true;

        for (int i = 0; i < info.length; i++) {
            if (info[i].length() == 0) {
                texts[i].setText("You cannot leave this field blank");
                isValid = false;
            } else {
                if (i == 1 && info[i].length() != MAX_ID_LENGTH) { // element at position 1 is Book ID
                    texts[i].setText("Book ID must consist of " + MAX_ID_LENGTH + " characters.");
                    isValid = false;
                } else if (i != 1 && info[i].length() > MAX_LENGTH) {
                    texts[i].setText("Text entered must not exceed a maximum length of " + MAX_LENGTH + ".");
                    isValid = false;
                }
            }
        }
        return isValid;
    }

    public boolean isBookAlreadyAdded(String id) {
        try {
            String SEARCH = "SELECT * FROM " + handler.getBookTableName()
                    + " WHERE id = '" + id + "'";
            ResultSet result = handler.execQuery(SEARCH);
            if (result.next()) {
                FxAlerts.showError("Failed","This book already exists.\nPlease add a diffrent Book ID.");
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddBookController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void getBookDataInEdit(Book book) {
        title.setText(book.getTitle());
        id.setText(book.getId());
        author.setText(book.getAuthor());
        publisher.setText(book.getPublisher());
        id.setEditable(false);
        isEditing = Boolean.TRUE;
    }

    private void editBook() {
        Book aBook = new Book(title.getText(), id.getText(), author.getText(), publisher.getText(), true);
        if (handler.editBook(aBook)) {
            FxAlerts.showInformation("Completed", "Book '" + aBook.getTitle() + "' (ID: " + 
                                     aBook.getId() + ") is edited.");
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
        } else {
            FxAlerts.showError("Error", "Cannot edit the book information, please revise the information");
        }
    }


    private final int NUM_OF_FIELDS = 4;
    private final int MAX_ID_LENGTH = 4;
    private final int MAX_LENGTH = 100;
}
