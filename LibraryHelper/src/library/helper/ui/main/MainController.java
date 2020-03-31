/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.helper.ui.main;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.helper.database.DatabaseHandler;
import library.helper.fxdialogs.FxAlerts;
import library.helper.ui.addbook.AddBookController;
import library.helper.ui.addmember.Add_memberController;
//import library.assistant.ui.booklist.Book_listController;
import library.helper.ui.booklist.Book;
import library.helper.ui.booklist.Book_listController;
import library.helper.ui.memberlist.Member;
import library.helper.ui.memberlist.MemberListController;
import library.helper.ui.utility.LibraryHelperUtility;

/**
 * FXML Controller class
 *
 * @author kumaq
 */
public class MainController implements Initializable {

    DatabaseHandler handler;
    // "Books" tab
    @FXML private TableColumn<Book, String> titleCol;
    @FXML private TableColumn<Book, String> idCol;
    @FXML private TableColumn<Book, String> authorCol;
    @FXML private TableColumn<Book, String> publisherCol;
    @FXML private TableColumn<Book, String> availCol;
    @FXML private TableView<Book> bookTable;
    @FXML private Button addBookButton;
    @FXML private TextField searchBookField;
    private ObservableList<Book> bookData = FXCollections.observableArrayList();
    
    //"Members" tab
    @FXML private TableView<Member> memberTable;
    @FXML private TableColumn<Member, String> mNameCol;
    @FXML private TableColumn<Member, String> mIdCol;
    @FXML private TableColumn<Member, String> mEmailCol;
    @FXML private TableColumn<Member, String> mPhoneCol;
    @FXML private TextField searchMemberField;
    @FXML private Button addMemberButton;
    private ObservableList<Member> memberData = FXCollections.observableArrayList();
    
    // "Book Issue" tab
    @FXML private TableView<Book> bookCartTable;
    @FXML private TableColumn<Book, String> cart_titleCol;
    @FXML private TableColumn<Book, String> cart_idCol;
    @FXML private TableColumn<Book, String> cart_authorCol;
    @FXML private TableColumn<Book, String> cart_publisherCol;
    @FXML private Button addToCartButton;
    @FXML private Button issueBookButton;
    @FXML private Button clearCartButton;
    @FXML private Text nameDes;
    @FXML private Text emailDes;
    @FXML private Text phoneDes;
    @FXML private Text titleDes;
    @FXML private Text authorDes;
    @FXML private Text publisherDes;
    @FXML private Text availDes;
    @FXML private TextField member_id_entered;
    @FXML private TextField book_id_entered;
    @FXML private Text member_id_error;
    @FXML private Text book_id_error;
    private ObservableList<Book> bookCart = FXCollections.observableArrayList();
    @FXML private Button removeCartButton;
    
    // Renew/Submission tab    
    @FXML private Text nameR;
    @FXML private Text emailR;
    @FXML private TextField memberID_input;
    @FXML private TableView<IssuedBook> IssuedBookTable;
    private ObservableList<Book> IssueBooks = FXCollections.observableArrayList();
    @FXML private Text memberError;
    @FXML private Text phoneR;
    @FXML private TableColumn<?, ?> rTitleCol;
    @FXML private TableColumn<?, ?> rBookIdCol;
    @FXML private TableColumn<?, ?> rAuthorCol;
    @FXML private TableColumn<?, ?> rPubCol;
    @FXML private TableColumn<?, ?> rIssuedTimeCol;
    @FXML private TableColumn<?, ?> rRenewCountCol;
    @FXML private Button returnButton;
    @FXML private Button renewButton;
    ObservableList<IssuedBook> checkoutData = FXCollections.observableArrayList();
    
    // panes and tabs
    @FXML private TabPane tabPane;
    @FXML private StackPane rootPane;
    @FXML private Tab bookTab;
    @FXML private Tab memberTab;
    @FXML private Tab issueTab;
    @FXML private Tab renewTab;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handler = DatabaseHandler.getHandlerObject();
        // initialize tables
        handler.initBookData();
        handler.initMemberData();
        initBookTable();
        initMemberTable();
        initBookCartTable();
        initIssuedBookTable();
        // load data into tables
        loadBookData();
        loadMemberData();
        // disable some buttons (which can only be clicked under specific situations)
        initButtons();
    }
    private void initButtons(){
        addToCartButton.setDisable(true);
        removeCartButton.setDisable(true);
        clearCartButton.setDisable(true);
        returnButton.setDisable(true);
        renewButton.setDisable(true);
    }
    // initialize book table with 5 columns
    private void initBookTable() {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        availCol.setCellValueFactory(new PropertyValueFactory<>("availability"));
        bookTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
    }
    // initalize member table with 4 columns
    private void initMemberTable() {
        mNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        mPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        memberTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
    }
    // initialize book cart with 4 columns
    private void initBookCartTable() {
        cart_titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        cart_idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        cart_authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        cart_publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        bookCartTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        
        //tb1_availCol.setCellValueFactory(new PropertyValueFactory<>("availability"));
    }
    // initialize table of issued books with 6 columns
    private void initIssuedBookTable() {
        rTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        rBookIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        rAuthorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        rPubCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        rIssuedTimeCol.setCellValueFactory(new PropertyValueFactory<>("issuedTime"));
        rRenewCountCol.setCellValueFactory(new PropertyValueFactory<>("renewCount"));
        IssuedBookTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
    }
    // populate book table with book data
    private void loadBookData() {
        try {
            String query = "SELECT * FROM " + handler.getBookTableName();
            ResultSet result = handler.execQuery(query);
            while (result.next()) {
                String title = result.getString("title");
                String id = result.getString("id");
                String author = result.getString("author");
                String publisher = result.getString("publisher");
                Boolean avail = result.getBoolean("isAvail");
                bookData.add(new Book(title, id, author, publisher, avail)); 
            }
            bookTable.setItems(bookData);
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // populate member table with member data
    private void loadMemberData() {
        try {
            String query = "SELECT * FROM " + handler.getMemberTableName();
            ResultSet result = handler.execQuery(query);
            while (result.next()) {
                String name = result.getString("name");
                String id = result.getString("id");
                String email = result.getString("email");
                String phone = result.getString("phone");
                memberData.add(new Member(name, id, email, phone));
            }
            memberTable.setItems(memberData);
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // display a window that allows admin to add a book to the database
    // if admin adds a book, update the table
    @FXML
    private void displayAddBook(ActionEvent event) {
        loadScene("/library/helper/ui/addbook/AddBook.fxml", "Add New Book");
        refreshBookTable();
    }
    // load a new JavaFx window
    public void loadScene(String location, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
            Parent parent = loader.load();
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.showAndWait();
            //return loader;
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            //return null;
        }
    }
    // search a book in the database by trying to
    // match input string with every column in the database
    @FXML
    private void searchBook(KeyEvent event) {
        FilteredList<Book> filteredData = new FilteredList<>(bookData, p -> true);

        // case 1: displays result as input string changes
        searchBookField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(book -> {
                // if filter is empty (no input string), show the whole database
                if (newValue.isEmpty() || newValue == null) {
                    return true;
                } // otherwise, look for the input string in every column of the database
                return book.containsSomeText(newValue.toLowerCase());
            });
        });
        // case 2: display result when the person hits enter
        searchBookField.setOnAction((e) -> {
            String newValue = searchBookField.getText().trim();
            filteredData.setPredicate(new Predicate<Book>() {
                @Override
                public boolean test(Book book) {
                    // if filter is empty
                    if (newValue.isEmpty() || newValue == null) {
                        return true;
                    } // otherwise
                    return book.containsSomeText(newValue.toLowerCase());
                }
            });
        });
        SortedList<Book> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(bookTable.comparatorProperty());
        bookTable.setItems(sortedData);
    }

    // search a member in the database by trying to match
    // input string with every column in the database
    @FXML
    private void searchMember(KeyEvent event) {
        FilteredList<Member> filteredData = new FilteredList<>(memberData, p -> true);

        // case 1: displays result as input string changes
        searchMemberField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(member -> {
                // if filter is empty
                if (newValue.isEmpty() || newValue == null) {
                    return true;
                } // otherwise
                return member.containsSomeText(newValue.toLowerCase());
               
            });
        });
        // case 2: displays result when the person hits enter
        searchMemberField.setOnAction((e) -> {
            String newValue = searchMemberField.getText().trim();
            filteredData.setPredicate(new Predicate<Member>() {
                public boolean test(Member member) {
                    // if filter is empty
                    if (newValue.isEmpty() || newValue == null) {
                        return true;
                    } // otherwise
                    return member.containsSomeText(newValue.toLowerCase());
                }
            });
        });
        SortedList<Member> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(memberTable.comparatorProperty());

        memberTable.setItems(sortedData);
    }
    // displays a window where admin can add a member to the database
    // if admin adds a member, update the table
    @FXML
    private void displayAddMember(ActionEvent event) {
        loadScene("/library/helper/ui/addmember/add_member.fxml", "Add New Member");
        refreshMemberTable();
    }
    // read input string (member id) and retrieve info
    // of a member from the database
    @FXML
    private void retrieveMember(ActionEvent event) {
        member_id_error.setText("");
        String id = member_id_entered.getText().trim();
        
        if (id.isEmpty() || id == null) { // display error if no member id is entered
            member_id_error.setText("Please enter a Member ID");
            return;
        }
        String query = "Select * from " + handler.getMemberTableName() + " where id = '" + id + "'";
        ResultSet rs = handler.execQuery(query);
        Boolean isAvailable = false;
        try {
            while (rs.next()) {
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                nameDes.setText(name);
                emailDes.setText(email);
                phoneDes.setText(phone);
                isAvailable = true;
            }
            if (!isAvailable) {
                member_id_error.setText("This member does not exist.");
                clearMemberDescription();

            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // read input string (book id) and retrieve info of
    // a book from the database
    @FXML
    private void retrieveBook(ActionEvent event) {
        book_id_error.setText("");
        String id = book_id_entered.getText().trim();
        // if no book id is entered
        if (id.isEmpty() || id == null) {
            book_id_error.setText("Please enter a Book ID");
            return;
        }
        // if book id does not contain 4 characters
        if (id.length() != MAX_BOOK_ID_LENGTH) {
            book_id_error.setText("Book ID must contain 4 characters");
            return;
        }
        String query = "Select * from " + handler.getBookTableName() + " where id = '" + id + "'";
        ResultSet rs = handler.execQuery(query);
        Boolean isAvailable = false;
        try {
            while (rs.next()) {
                String name = rs.getString("title");
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");
                Boolean status = rs.getBoolean("isAvail");
                isAvailable = true;

                titleDes.setText(name);
                authorDes.setText(author);
                publisherDes.setText(publisher);
                if (status) {
                    availDes.setText("Available");
                    addToCartButton.setDisable(false);

                } else {
                    availDes.setText("Not available!");
                }
            }
            if (!isAvailable) {
                book_id_error.setText("This book does not exist.");
                clearBookDescription();

            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // clear a member's description
    public void clearMemberDescription() {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        if (currentTab == issueTab){
            nameDes.setText("N/A");
            emailDes.setText("N/A");
            phoneDes.setText("N/A");
        }
        else if (currentTab == renewTab){
            nameR.setText("N/A");
            emailR.setText("N/A");
            phoneR.setText("N/A");
        }
    }

    public void clearBookDescription() {
        titleDes.setText("N/A");
        authorDes.setText("N/A");
        publisherDes.setText("N/A");
        availDes.setText("N/A");
    }

    @FXML
    private void clearCart(ActionEvent event) {
        bookCart.clear();
        bookCartTable.getItems().clear();
        addToCartButton.setDisable(true);
        removeCartButton.setDisable(true);
        clearCartButton.setDisable(true);
    }

    public void clearIssuePage() {
        member_id_entered.clear();
        member_id_error.setText("");
        clearMemberDescription();

        book_id_entered.clear();
        book_id_error.setText("");
        clearBookDescription();
        bookCart.clear();
        bookCartTable.getItems().clear();
        addToCartButton.setDisable(true);
        removeCartButton.setDisable(true);
        clearCartButton.setDisable(true);
    }

    // add a book to the list/cart when admin clicks "Add to to cart" button
    @FXML
    private void addBookToCart(ActionEvent event) {
        // book is already valid so we only need to check if it is already in the cart
        String id = book_id_entered.getText().trim();
        if (isBookInCart(id, bookCart)) {
            return;
        }
        String query = "Select * from " + handler.getBookTableName() + " where id = '" + id + "'";
        ResultSet rs = handler.execQuery(query);
        try {
            while (rs.next()) {
                String name = rs.getString("title");
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");
                Boolean status = rs.getBoolean("isAvail");
                bookCart.add(new Book(name, id, author, publisher, status));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        // display books in the cart
        bookCartTable.setItems((bookCart));
        removeCartButton.setDisable(false);
        clearCartButton.setDisable(false);        
    }

    @FXML
    private void removeBookFromCart(ActionEvent event) {
        ObservableList<Book> cartlist = bookCartTable.getSelectionModel().getSelectedItems();
        if(cartlist.isEmpty()){
            FxAlerts.showError("None selected", "Please select at least 1 book to delete.");
            return;
        }else{
            removeCartButton.setDisable(false);
        }
        //remove selected books from book cart
        bookCart.removeAll(cartlist);
        if (bookCart.size() == 0){
            removeCartButton.setDisable(true);
            clearCartButton.setDisable(true);
        }
    }

    @FXML
    private void disableAddButtonWhileTyping(KeyEvent event) {
        addToCartButton.setDisable(true);
    }

    // check if a book is already in the cart
    public boolean isBookInCart(String id, ObservableList<Book> cart) {
        if (cart.size() > 0) {
            for (Book b : cart) {
                if (id.equals(b.getId())) {
                    FxAlerts.showError("Repeated Item", "This book is already in the cart.");
                    return true;
                }
            }
        }
        return false;
    }
    public boolean isCartEmpty(){
        if (bookCart.size() == 0){
            FxAlerts.showError("Empty Cart","Please add at least 1 book to the cart.");
            return true;
        }
        return false;
    }

    @FXML
    private void issueBook(ActionEvent event) {
        if (isMemberIdEntered() && !isCartEmpty()) {
            String confirmMessage = "Is " + nameDes.getText() + " checking out " + bookCart.size() + " book(s)?";

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Checkout Confirmation");
            alert.setHeaderText(null);
            alert.setContentText(confirmMessage);

            Optional<ButtonType> answer = alert.showAndWait();
            if (answer.get() == ButtonType.OK) {
                for (int i = 0; i < bookCart.size(); i++) {
                    String bookId = bookCart.get(i).getId();
                    String memberId = member_id_entered.getText();

                    String query1 = "INSERT INTO CHECKOUT(bookId, memberId) VALUES ( "
                            + "'" + bookId + "',"
                            + "'" + memberId + "')";

                    String query2 = "UPDATE BOOK SET isAvail = false WHERE id = '" + bookId + "'";
                    if (!handler.execAction(query1) || !handler.execAction(query2)) {
                        FxAlerts.showError("Checkout Failed", "Unable to issue Book ID  " + bookId + ".");
                        clearIssuePage();
                        return;
                    }
                    //handler.execAction(query2);
                }
                FxAlerts.showInformation("Checkout Completed", "Successfully issued " 
                                        + bookCart.size() + " book(s) to " + nameDes.getText() + ".");
                clearIssuePage();
                refreshBookTable();

            } else {
                FxAlerts.showError("Cancellation", "Checkout cancelled.");
                //(Alert.AlertType.INFORMATION, "Cancellation", null, "Checkout cancelled");
            }
        }

    }

    public boolean isMemberIdEntered() {
        if (nameDes.getText().equals("N/A") && emailDes.getText().equals("N/A") && phoneDes.getText().equals("N/A")) {
            FxAlerts.showError("Failed", "Please confirm a Member ID before checking out.");
            return false;
        }
        return true;
    }

    public void refreshBookTable() {
        bookData.clear();
        loadBookData();
    }
    public void refreshMemberTable(){
        memberData.clear();
        loadMemberData();
    }

    @FXML
    private void deleteSelection(ActionEvent event) {
        String pane = tabPane.getSelectionModel().getSelectedItem().getId();

        if (pane.equals(bookTab.getId())) {
            //Highlight the selected book's row
            ObservableList<Book> selectedBooks = FXCollections.observableArrayList();
            selectedBooks = bookTable.getSelectionModel().getSelectedItems();
//            Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
            if (selectedBooks.isEmpty()) {
                FxAlerts.showError("No book selected", "Please select a book to delete");
                return;
            }
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Deleting book");
            confirmation.setContentText("Are you sure you want to delete selected book(s)?");
            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.get() == ButtonType.OK) {
                StringBuilder books = new StringBuilder("Book ID: ");
                boolean status = false;
                for (Book selectedBook : selectedBooks) {
                    if (DatabaseHandler.getHandlerObject().isCheckedOut(selectedBook)) {
                        FxAlerts.showError("Deletion Failed", "This book is checked out at the moment and can't be erased.");
                        return;
                    }

                    boolean succes = DatabaseHandler.getHandlerObject().removeBook(selectedBook);
                    if (succes) {
                        status = true;
                        books.append(selectedBook.getId() + "\n");
//                        bookData.remove(selectedBook);
                    } else {
                        FxAlerts.showInformation("Deletion Failed", selectedBook.getTitle() + "was unable to erase from the library.");
                    }
                }
                if (status) {
                    String bookStrings = books.toString().substring(0,books.length() - 2);
                    FxAlerts.showInformation("Book Removed", "These books were erased from the library:\n" + bookStrings);
                    bookData.clear();
                    loadBookData();
                }
            } else {
                FxAlerts.showInformation("Delete Cancellation", "Deletion is cancelled");
            }

        } else {
            ObservableList<Member> selectedMembers = FXCollections.observableArrayList();
            selectedMembers = memberTable.getSelectionModel().getSelectedItems();
//            Member selectedMember = memberTable.getSelectionModel().getSelectedItem();
            if (selectedMembers.isEmpty()) {
                FxAlerts.showError("No member selected", "Please select a member to delete");
                return;
            }
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Deleting member");
            confirmation.setContentText("Are you sure you want to delete selected member(s)?");
            Optional<ButtonType> result = confirmation.showAndWait();
            StringBuilder members = new StringBuilder("Member ID: ");
            boolean status = false;
            if (result.get() == ButtonType.OK) {
                for (Member selectedMember : selectedMembers) {
                    if (DatabaseHandler.getHandlerObject().isMemberRenting(selectedMember)) {
                        FxAlerts.showError("Deletion Failed", "This member is renting a book at the moment and can't be erased.");
                        return;
                    }

                    boolean succes = handler.removeMember(selectedMember);
                    if (succes) {
                        status = true;
                        members.append(selectedMember.getId() + "\n");
//                        memberData.remove(selectedMember);
                    } else {
                        FxAlerts.showError("Deletion Failed", selectedMember.getName() + "was unable to erase from the library.");
                    }
                }
                if (status) {
                    String memberStrings = members.toString().substring(0,members.length() -2);
                    FxAlerts.showInformation("Member Removed", "These members were erased from the library:\n" + memberStrings);
                    memberData.clear();
                    loadMemberData();
                }
            } else {
                FxAlerts.showInformation("Delete Cancellation", "Deletion is cancelled");
            }
        }
    }

    @FXML
    private void editSelection(ActionEvent event) {
        String pane = tabPane.getSelectionModel().getSelectedItem().getId();
        if (pane.equals(bookTab.getId())) {
            Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
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
                    refreshTable(new ActionEvent());
                });

            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Member selectedMember = memberTable.getSelectionModel().getSelectedItem();
            if (selectedMember == null) {
                FxAlerts.showError("No member selected", "Please select a member to edit.");
                return;
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/helper/ui/addmember/add_member.fxml"));
                Parent parent = loader.load();

                Add_memberController controller = (Add_memberController) loader.getController();
                controller.getMemberDataInEdit(selectedMember);

                Stage stage = new Stage(StageStyle.DECORATED);
                stage.setTitle("Edit");
                stage.setScene(new Scene(parent));
                stage.show();
                LibraryHelperUtility.setSceneIcon(stage);

                stage.setOnHiding((e) -> {
                    refreshTable(new ActionEvent());
                });

            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void refreshTable(ActionEvent event) {
        refreshData();
    }

    private void refreshData() {
        String pane = tabPane.getSelectionModel().getSelectedItem().getId();
        if (pane.equals(bookTab.getId())) {
            bookData.clear();
            loadBookData();
        } else {
            memberData.clear();
            loadMemberData();
        }
    }

    @FXML
    private void runMenuClose(ActionEvent event) {
        ((Stage) rootPane.getScene().getWindow()).close();
    }

    @FXML
    private void runSettings(ActionEvent event) {
        loadScene("/library/helper/settingsButton/SettingButton.fxml", "Settings");
    }
   

    @FXML
    private void handlerMemberID(ActionEvent event) {
        returnButton.setDisable(true);
        renewButton.setDisable(true);
        checkoutData.clear();
        isReturnable = false;
        memberError.setText("");
        String memberId = memberID_input.getText().trim();
        if (memberId.isEmpty() || memberId == null) {
            memberError.setText("Please enter a Member ID");
            return;
        }

        String query = "Select * from " + handler.getMemberTableName() + " where id = '" + memberId + "'";
        ResultSet rs = handler.execQuery(query);
        Boolean isAvailable = false;
        try {
            while (rs.next()) {
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                nameR.setText(name);
                emailR.setText(email);
                phoneR.setText(phone);
                isAvailable = true;
            }
            if (!isAvailable) {
                memberError.setText("This member does not exist.");
                clearMemberDescription();
                checkoutData.clear();
                IssuedBookTable.setItems(checkoutData);
                return;
            } else {
                returnButton.setDisable(false);
                renewButton.setDisable(false);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadCheckoutTable();
    }

    private void loadCheckoutTable() {
        String memberId = memberID_input.getText().trim();
        String query1 = "SELECT * FROM CHECKOUT INNER JOIN BOOK"
                + " ON CHECKOUT.bookId = BOOK.id"
                + " WHERE CHECKOUT.memberId = '" + memberId + "'";
        System.out.println("query1: " + query1);
        ResultSet rs1 = handler.execQuery(query1);
        try {
            while (rs1.next()) {
                String title = rs1.getString("title");
                String bookId = rs1.getString("bookId");
                String author = rs1.getString("author");
                String publisher = rs1.getString("publisher");
                Timestamp checkout_time = rs1.getTimestamp("checkout_time");
                int renew_number = rs1.getInt("renew_number");
                checkoutData.add(new IssuedBook(title, bookId, author, publisher, checkout_time.toGMTString(), String.valueOf(renew_number)));
            }
            isReturnable = true;
            IssuedBookTable.setItems(checkoutData);
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void runReturn(ActionEvent event) {
        if (isReturnable) {
            ObservableList<IssuedBook> selectedReturn = FXCollections.observableArrayList();
            selectedReturn = IssuedBookTable.getSelectionModel().getSelectedItems();
            if (selectedReturn.isEmpty()) {
                FxAlerts.showError("No book selected", "Please select at least one book to submit.");
                return;
            }
            StringBuilder bookIds = new StringBuilder();
            for (IssuedBook sr : selectedReturn) {
                bookIds.append(sr.getId() + ", ");
            }
            String bookIdStrings = bookIds.toString().substring(0, bookIds.length() - 2);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Return");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to return the following book ID(s): " + bookIdStrings + "?");

            Optional<ButtonType> answer = alert.showAndWait();
            if (answer.get() == ButtonType.OK) {
                boolean submitStatus = false;
                String[] bookIdArray = bookIds.toString().trim().split("\\s*,\\s*");
                for (String bId : bookIdArray) {
                    String query = "DELETE FROM CHECKOUT WHERE bookId = '" + bId + "'";
                    String query1 = "UPDATE BOOK SET isAvail = TRUE WHERE id = '" + bId + "'";
                    if (handler.execAction(query) && handler.execAction(query1)) {
                        submitStatus = true;
                    } else {
                        submitStatus = false;
                    }
                }
                if (submitStatus) {
                    FxAlerts.showInformation("Success", "Submission completed");
                    checkoutData.clear();
                    loadCheckoutTable();
                    refreshBookTable();
                } else {
                    FxAlerts.showError("Failed", "Submission failed");
                }
            } else {
                FxAlerts.showError("Cancelled", "Submission cancelled");
            }
        } else {
            FxAlerts.showError("Failed", "Book Id not found");
            return;
        }
    }

    @FXML
    private void runRenew(ActionEvent event) {
        if (isReturnable) {
            ObservableList<IssuedBook> selectedRenew = FXCollections.observableArrayList();
            selectedRenew = IssuedBookTable.getSelectionModel().getSelectedItems();
            if (selectedRenew.isEmpty()) {
                FxAlerts.showError("No book selected", "Please select at least one book to renew.");
                return;
            }
            StringBuilder bookIds = new StringBuilder();
            for (IssuedBook sr : selectedRenew) {
                bookIds.append(sr.getId() + ", ");
            }
            String bookIdStrings = bookIds.toString().substring(0, bookIds.length() - 2);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Return");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to renew the following book ID(s): " + bookIdStrings + "?");

            Optional<ButtonType> answer = alert.showAndWait();
            if (answer.get() == ButtonType.OK) {
                boolean renewStatus = false;
                String[] bookIdArray = bookIds.toString().trim().split("\\s*,\\s*");
                for (String bId : bookIdArray) {
                    String query = "UPDATE CHECKOUT SET checkout_time = CURRENT_TIMESTAMP, renew_number = renew_number+1 WHERE bookId = '" + bId + "'";
                    if (handler.execAction(query)) {
                        renewStatus = true;
                    } else {
                        renewStatus = false;
                    }
                }
                if (renewStatus) {
                    FxAlerts.showInformation("Success", "Renewal complete");
                    checkoutData.clear();
                    loadCheckoutTable();
                } else {
                    FxAlerts.showError("Failed", "Renewal failed");
                }
            } else {
                FxAlerts.showError("Cancelled", "Renewal cancelled");
            }
        } else {
            FxAlerts.showError("Failed", "Book Id not found");
            return;
        }
    }
     private final int MAX_BOOK_ID_LENGTH = 4;
     private Boolean isReturnable = false;

}
