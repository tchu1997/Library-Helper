/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package library.helper.ui.addmember;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import library.helper.database.DatabaseHandler;
import library.helper.fxdialogs.FxAlerts;
import library.helper.ui.memberlist.Member;

/**
 * FXML Controller class
 *
 * @author kumaq
 */
public class Add_memberController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXTextField name;
    @FXML
    private Text nameErr;
    @FXML
    private JFXTextField id;
    @FXML
    private Text idErr;
    @FXML
    private JFXTextField email;
    @FXML
    private Text emailErr;
    @FXML
    private JFXTextField phone;
    @FXML
    private Text phoneErr;
    @FXML
    private JFXButton addButton;
    @FXML
    private JFXButton cancelButton;
    DatabaseHandler handler;
    private Boolean isEditing = Boolean.FALSE;
   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handler = DatabaseHandler.getHandlerObject();
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void addMember(ActionEvent event) {
        String mName = name.getText();
        String mID = id.getText();
        String mEmail = email.getText();
        String mPhone = phone.getText();

        if (isEditing) {
            editMember();
            return;
        }

        if (isMemberInfoValid(mName, mID, mEmail, mPhone) && !isMemberAlreadyAdded(mID)) {
            String INSERT = "INSERT INTO " + handler.getMemberTableName() + " VALUES ("
                    + "'" + mID + "',"
                    + "'" + mName + "',"
                    + "'" + mPhone + "',"
                    + "'" + mEmail + "'"
                    + ")";
            if (handler.execAction(INSERT)) {
                FxAlerts.showInformation("Confirmation","Member is successfully added.");
                // close add-book gui after boos is successfully added
                Stage stage = (Stage) rootPane.getScene().getWindow();
                stage.close();
            } else {
                FxAlerts.showError("Failed","An error occurred.\nThis person cannot be added.");
            }
        }
    }

    public boolean isMemberAlreadyAdded(String id) {
        try {
            String SEARCH = "SELECT * FROM " + handler.getMemberTableName()
                    + " WHERE id = '" + id + "'";
            ResultSet result = handler.execQuery(SEARCH);
            if (result.next()) {
                FxAlerts.showError("Failed","This member already exists.\nPlease add a diffrent member ID.");
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Add_memberController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean isMemberInfoValid(String name, String id, String email, String phone) {
        String info[] = {name, id, email, phone};
        Text texts[] = {nameErr, idErr, emailErr, phoneErr};
        boolean isValid = true;

        for (int i = 0; i < info.length; i++) {
            if (info[i].length() == 0) {
                texts[i].setText("You cannot leave this field blank");
                isValid = false;
            } else {
                if (i == 1 && info[i].length() != MAX_ID_LENGTH) { // element at position 1 is Member ID
                    texts[i].setText("Member ID must consist of " + MAX_ID_LENGTH + " characters.");
                    isValid = false;
                } else if (i != 1 && info[i].length() > MAX_LENGTH) {
                    texts[i].setText("Text entered must not exceed a maximum length of " + MAX_LENGTH + ".");
                    isValid = false;
                }
            }
        }
        return isValid;
    }

    public void getMemberDataInEdit(Member member) {
        name.setText(member.getName());
        id.setText(member.getId());
        phone.setText(member.getPhone());
        email.setText(member.getEmail());
        id.setEditable(false);
        isEditing = Boolean.TRUE;
    }

    private void editMember() {
        Member aMember = new Member(name.getText(), id.getText(), email.getText(), phone.getText());

        if (DatabaseHandler.getHandlerObject().editMember(aMember)) {
            FxAlerts.showInformation("Completed", "Member '" + aMember.getName() 
                                + "' (ID: " + aMember.getId() + ") is edited.");
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
        } else {
            FxAlerts.showError("Error", "Cannot edit the member information, please revise the information.");
        }
    }
   
    private final int MAX_ID_LENGTH = 4;
    private final int MAX_LENGTH = 100;

}
