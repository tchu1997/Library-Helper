/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.helper.ui.memberlist;

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
import library.helper.ui.addmember.Add_memberController;
import library.helper.ui.main.MainController;
import library.helper.ui.utility.LibraryHelperUtility;

/**
 * FXML Controller class
 *
 * @author leduy
 */
public class MemberListController implements Initializable {

    @FXML
    private TableView<Member> tableView;
    @FXML
    private TableColumn<Member, String> nameCol;
    @FXML
    private TableColumn<Member, String> idCol;
    @FXML
    private TableColumn<Member, String> phoneCol;
    @FXML
    private TableColumn<Member, String> emailCol;

    @FXML
    private AnchorPane rootPane;

    DatabaseHandler handler;
    ObservableList<Member> memberList = FXCollections.observableArrayList();

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
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void loadData() {
        memberList.clear();
        String qu = "SELECT * FROM " + handler.getMemberTableName();
        ResultSet rs = handler.execQuery(qu);
        try {
            while (rs.next()) {
                String name = rs.getString("name");
                String id = rs.getString("id");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                memberList.add(new Member(name, id, phone, email));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MemberListController.class.getName()).log(Level.SEVERE, null, ex);
        }

        tableView.setItems(memberList);
    }

    @FXML
    private void deleteMemberSelection(ActionEvent event) {
        //Highlight the selected member's row
        Member selectedMember = tableView.getSelectionModel().getSelectedItem();
        if (selectedMember == null) {
            FxAlerts.showError("No member selected", "Please select at least one member to delete.");
            return;
        }

        if (DatabaseHandler.getHandlerObject().isMemberRenting(selectedMember)) {
            FxAlerts.showError("Deletion Failed", "This member is renting a book at the moment and can't be erased.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Deleting Member");
        confirmation.setContentText("Are you sure  you  want to delete the selected member(s) " + selectedMember.getName() + " ?");
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.get() == ButtonType.OK) {
            boolean succes = handler.removeMember(selectedMember);
            if (succes) {
                FxAlerts.showInformation("Member deletion", selectedMember.getName() + "was erased from the library.");
                memberList.remove(selectedMember);
            } else {
                FxAlerts.showError("Deletion Failed", selectedMember.getName() + "was unable to erase from the library.");
            }
        } else {
            FxAlerts.showInformation("Delete Cancellation", "Deletion is cancelled.");
        }
    }

    @FXML
    private void editMemberSelection(ActionEvent event) {
        //Highlight the selected member's row
        Member selectedMember = tableView.getSelectionModel().getSelectedItem();
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
                refreshMemberSelection(new ActionEvent());
            });

        } catch (IOException ex) {
            Logger.getLogger(MemberListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void refreshMemberSelection(ActionEvent event) {
        loadData();
    }

}
