package library.helper.database;

//import jdk.nashorn.internal.AssertsEnabled.ir.Statement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javax.swing.JOptionPane;
import library.helper.ui.booklist.Book;
import library.helper.ui.memberlist.Member;

/**
 *
 * @author kumaq
 */
public class DatabaseHandler {

    private static DatabaseHandler handler;

    private static final String URL = "jdbc:derby:database;create=true";
    private static Connection connect = null;
    private static Statement statement = null;
    private static final String CONNECTION_PATH = "org.apache.derby.jdbc.EmbeddedDriver";

    private String BOOK_TABLE_NAME = "BOOK";
    private String MEMBER_TABLE_NAME = "MEMBER";
    private String CHECKOUT_TABLE_NAME = "CHECKOUT";

    private DatabaseHandler() {
        createConnection();
        createBookTable();
        createMemberTable();
        createCheckOutTable();
    }

    public static DatabaseHandler getHandlerObject() {
        return (handler == null) ? new DatabaseHandler() : handler;
    }

    public void createConnection() {
        try {
            // 1. load driver
            Class.forName(CONNECTION_PATH).newInstance();
            // 2. connect to a database
            connect = DriverManager.getConnection(URL);
            System.out.println("Successfully connected to database");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database connectiton failed", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
            System.err.println("DatabaseHandler.createConnection(): " + e.getMessage());
            System.exit(0);
        }
    }

    public void createBookTable() {
        try {
            statement = connect.createStatement();
            DatabaseMetaData dbmd = connect.getMetaData();
            ResultSet table = dbmd.getTables(null, null, BOOK_TABLE_NAME.toUpperCase(), null);
            if (!table.next()) {
                String CREATE_TABLE = "CREATE TABLE " + BOOK_TABLE_NAME + "("
                        + "id CHAR(4) NOT NULL PRIMARY KEY,\n" // book ID is limited to 4 characters
                        + "title VARCHAR(100) NOT NULL,\n"
                        + "author VARCHAR(100) NOT NULL,\n"
                        + "publisher VARCHAR(100) NOT NULL\n,"
                        + "isAvail boolean default true"
                        + " )";
                statement.execute(CREATE_TABLE);
            } else {
                System.out.println("Table " + BOOK_TABLE_NAME + " already exists.");
                //String DROP_TABLE = "DROP TABLE " + BOOK_TABLE_NAME;
                //statement.execute(DROP_TABLE);
            }
        } catch (SQLException e) {
            System.err.println("DatabaseHandler.createBookTable(): " + e.getMessage());
        }
    }

    public String getBookTableName() {
        return BOOK_TABLE_NAME;
    }

    public String getMemberTableName() {
        return MEMBER_TABLE_NAME;
    }

    public ResultSet execQuery(String s) {
        ResultSet result = null;
        try {
            statement = connect.createStatement();
            result = statement.executeQuery(s);
        } catch (SQLException e) {
            System.err.println("DatabaseHandler.execQuery(): " + e.getMessage());
        }
        return result;
    }

    public void initBookData() {
        String[] bookIDs = new String[]{"1001", "1002", "1003", "1004", "1005","1006","1007"};
        String[] bookTitles = new String[]{"Harry Potter", "Programming Python", "Philosophy:the basics", "Java: the complete reference", "JavaScript: kurz & gut", "Art: tempo of today","Love Story"};
        String[] bookAuthors = new String[]{"Rowling, JK", "Lutz, Mark ", "Warburton, Nigel", "Schildt, Herbert", "Flanagan, David","Morman, Jean Mary","Erich Segal"};
        String[] bookPublishers = new String[]{"Bloomsbury", "O Reilly Media", "Routledge", "McGraw-Hill Education", "O Reilly Media","Blauvelt, N.Y.","Harper & Row"};
        for (int i = 0; i < bookIDs.length; i++) {
            String INSERT = "INSERT INTO " + BOOK_TABLE_NAME + " VALUES ("
                    + "'" + bookIDs[i] + "',"
                    + "'" + bookTitles[i] + "',"
                    + "'" + bookAuthors[i] + "',"
                    + "'" + bookPublishers[i] + "',"
                    + "'" + true + "'"
                    + ")";
            this.execAction(INSERT);
        }
    }

    public void createMemberTable() {
        try {
            statement = connect.createStatement();
            DatabaseMetaData dbmd = connect.getMetaData();
            ResultSet table = dbmd.getTables(null, null, MEMBER_TABLE_NAME.toUpperCase(), null);
            if (table.next()) {
                System.out.println("Table " + MEMBER_TABLE_NAME + " already exists.");
            } else {
                statement.execute("CREATE TABLE " + MEMBER_TABLE_NAME + "("
                        + "  id VARCHAR(200) PRIMARY KEY,\n"
                        + "  name VARCHAR(150),\n"
                        + "  phone VARCHAR(20),\n"
                        + "  email VARCHAR(100)\n"
                        + " )");
            }
        } catch (SQLException e) {
            System.err.println("DatabaseHandler.createMemberTable(): " + e.getMessage());
        }
    }

    public void initMemberData() {
        String[] mIDs = new String[]{"1221", "1222", "1223", "1224", "1225","1226"};
        String[] mNames = new String[]{"Duy Le","Tuong Chu","Duy Tran","John Smith","Ivan Hernandez","Alan Wiliams"};
        String[] mPhones = new String[]{"(408)080-272","(669)212-294","(272)972-232","(669)101-900","(223)212-111","(249)898-312"};
        String[] mEmails = new String[]{"leduy1000@gmail.com","tuongchu99@gmail.com","duytran01@yahoo.com","johnsmith87@gmail.com","ivanhdz@yahoo.com","alanwl99@gmail.com"};
        for (int i = 0; i < mIDs.length; i++) {
            String INSERT = "INSERT INTO " + MEMBER_TABLE_NAME + " VALUES ("
                    + "'" + mIDs[i]+ "',"
                    + "'" + mNames[i] + "',"
                    + "'" + mPhones[i] + "',"
                    + "'" + mEmails[i] + "'"
                    + ")";
            this.execAction(INSERT);
        }
    }

    public boolean execAction(String qu) {
        try {
            statement = connect.createStatement();
            statement.execute(qu);
            return true;
        } catch (SQLException e) {
            System.err.println("DatabaseHandler.execAction(): " + e.getMessage());
        }
        return false;
    }

    void createCheckOutTable() {
        try {
            statement = connect.createStatement();
            DatabaseMetaData dbmd = connect.getMetaData();
            ResultSet table = dbmd.getTables(null, null, CHECKOUT_TABLE_NAME.toUpperCase(), null);

            if (table.next()) {
                System.out.println("Table " + CHECKOUT_TABLE_NAME + " already exists.");
            } else {
                statement.execute("CREATE TABLE " + CHECKOUT_TABLE_NAME + "("
                        + "  bookId cHAR(4) NOT NULL PRIMARY KEY,\n"
                        + "  memberId VARCHAR(200),\n"
                        + "  checkout_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"
                        + "  renew_number INTEGER DEFAULT 0,\n"
                        + "  FOREIGN KEY (bookId) REFERENCES BOOK(id),\n"
                        + "  FOREIGN KEY (memberId) REFERENCES MEMBER(id)"
                        + " )");
            }
        } catch (SQLException ex) {
            System.err.println("DatabaseHandler.createCheckOutTable(): " + ex.getMessage());
        }
    }

    public boolean removeBook(Book book) {
        try {
            String deleteStatement = "DELETE FROM BOOK WHERE ID = ?";
            PreparedStatement statement = connect.prepareStatement(deleteStatement);
            statement.setString(1, book.getId());
            int resume = statement.executeUpdate();
            if (resume == 1) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean removeMember(Member member) {
        try {
            String deleteStatement = "DELETE FROM MEMBER WHERE id = ?";
            PreparedStatement stmt = connect.prepareStatement(deleteStatement);
            stmt.setString(1, member.getId());
            int res = stmt.executeUpdate();
            if (res == 1) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean editBook(Book book) {
        try {
            String update = "UPDATE BOOK SET TITLE = ?, AUTHOR = ?, PUBLISHER = ? WHERE ID = ?";
            PreparedStatement stmt = connect.prepareStatement(update);
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getPublisher());
            stmt.setString(4, book.getId());
            int result = stmt.executeUpdate();
            return (result > 0);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean editMember(Member member) {
        try {
            String update = "UPDATE MEMBER SET NAME = ?, PHONE = ?, EMAIL = ? WHERE ID = ?";
            PreparedStatement stmt = connect.prepareStatement(update);
            stmt.setString(1, member.getName());
            stmt.setString(2, member.getPhone());
            stmt.setString(3, member.getEmail());
            stmt.setString(4, member.getId());
            int result = stmt.executeUpdate();
            return (result > 0);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean isCheckedOut(Book book) {
        try {
            String checkStatement = "SELECT COUNT(*) FROM CHECKOUT WHERE bookId=?";
            PreparedStatement state = connect.prepareStatement(checkStatement);
            state.setString(1, book.getId());
            ResultSet result = state.executeQuery();
            if (result.next()) {
                int i = result.getInt(1);
                System.out.println(i);
                return i > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean isMemberRenting(Member member) {
        try {
            String checkstmt = "SELECT COUNT(*) FROM CHECKOUT WHERE memberID=?";
            PreparedStatement stmt = connect.prepareStatement(checkstmt);
            stmt.setString(1, member.getId());
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                int i = result.getInt(1);
                System.out.println(i);
                return (i > 0);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
