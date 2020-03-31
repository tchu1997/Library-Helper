package library.helper.settingsButton;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author leduy
 */
public class SettingButtonController implements Initializable {

    @FXML
    private JFXTextField borrowPeriod;
    @FXML
    private JFXTextField finePerDay;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private Text borroeDayError;
    @FXML
    private Text fineError;
    @FXML
    private Text userNameError;
    @FXML
    private Text passwordError;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setDefaultValues();
    }

    @FXML
    private void SettingSaveHandler(ActionEvent event) {
        int bDay = Integer.parseInt(borrowPeriod.getText());
        double fine = Double.parseDouble(finePerDay.getText());
        String username = this.username.getText();
        String password = this.password.getText();

//        if(isSettingValid(username, password))
//        {
//        SettingWrapper settingWrapper = SettingWrapper.getSettingWrapper();
//        settingWrapper.setBorrowPeriod(bDay);
//        settingWrapper.setUsername(username);
//        settingWrapper.setPassword(password);
//        SettingWrapper.writeSettingtoFile(settingWrapper);
//        }
        SettingWrapper settingWrapper = SettingWrapper.getSettingWrapper();
        settingWrapper.setBorrowPeriod(bDay);
        settingWrapper.setUsername(username);
        settingWrapper.setPassword(password);
        SettingWrapper.writeSettingtoFile(settingWrapper);

    }

    @FXML
    private void SettingCancelHandler(ActionEvent event) {
        ((Stage) borrowPeriod.getScene().getWindow()).close();
    }

    private void setDefaultValues() {
        SettingWrapper settingWrapper = SettingWrapper.getSettingWrapper();
        borrowPeriod.setText(String.valueOf(settingWrapper.getBorrowPeriod()));
        finePerDay.setText(String.valueOf(settingWrapper.getFinePerDay()));
        username.setText(String.valueOf(settingWrapper.getUsername()));
        password.setText(String.valueOf(settingWrapper.getPassword()));
    }

//    public boolean isSettingValid(String userName, String password){
//        String[] info = {userName, password};
//        Text[] texts = {userNameError, passwordError};
//        boolean isValid = true;
//
//        for(int i = 0; i < info.length; i++){
//            if (info[i].length() == 0){
//                texts[i].setText("You cannot leave this field blank");
//                isValid = false;
//            } else {
//                if (i == 1 && info[i].length()>= MAX_PASSWORD_LENGTH){ // element at position 2 is Password
//                    texts[i].setText("Password must less than " + MAX_PASSWORD_LENGTH + " characters.");
//                    isValid = false;
//                }
//                else if(info[i].length() < MINIMUM_LENGTH)
//                {
//                    texts[i].setText("Must has at least " + MINIMUM_LENGTH + ".");
//                    isValid = false;
//                }
//                else if(i != 1 && info[i].length() > MAX_LENGTH)
//                {
//                    texts[i].setText("Text entered must not exceed a maximum length of " + MAX_LENGTH + ".");
//                    isValid = false;
//                }
//            }
//        }
//        return isValid;
//    }
//
//       public void warning_alert(String msg){
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setHeaderText(null);
//        alert.setContentText(msg);
//        alert.showAndWait();
//    }
//   public void information_alert(String msg){
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setHeaderText(null);
//        alert.setContentText(msg);
//        alert.showAndWait();
//    }
//
//    private final int MAX_PASSWORD_LENGTH = 16;
//    private final int MINIMUM_LENGTH = 4;
//    private final int MAX_LENGTH = 100;
}
