/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.helper.ui.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.helper.fxdialogs.FxAlerts;
import library.helper.settingsButton.SettingWrapper;
import library.helper.ui.utility.LibraryHelperUtility;
import org.apache.commons.codec.digest.DigestUtils;

public class LoginController implements Initializable {

    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;

    SettingWrapper settingWrapper;
    @FXML
    private Label title;
    @FXML
    private Text loginErr;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        settingWrapper = SettingWrapper.getSettingWrapper();
    }

    @FXML
    private void runLogin(ActionEvent event) {
        title.setText("Login");
        title.setStyle("-fx-text-fill: black;");
        String username = this.username.getText();
        String password = DigestUtils.shaHex(this.password.getText());

        if (username.equals(settingWrapper.getUsername()) && password.equals(settingWrapper.getPassword())) {
            closeStage();
            loadScene();
        } else {
            loginErr.setText("Invalid username or password");
        }

    }

    @FXML
    private void runCancel(ActionEvent event) {
        System.exit(0);
    }

    public void loadScene() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/library/helper/ui/main/Main.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Library Helper");
            stage.setScene(new Scene(parent));
            stage.show();
            LibraryHelperUtility.setSceneIcon(stage);
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void closeStage() {
        ((Stage)username.getScene().getWindow()).close();
    }

}
