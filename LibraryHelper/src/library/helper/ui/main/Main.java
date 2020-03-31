/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.helper.ui.main;

/**
 *
 * @author leduy
 */
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.helper.database.DatabaseHandler;
import library.helper.ui.utility.LibraryHelperUtility;

/**
 *
 * @author leduy
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("/library/helper/ui/main/draft.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/library/helper/ui/login/login.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
        stage.setTitle("Login");

        LibraryHelperUtility.setSceneIcon(stage);
        
        new Thread(() -> {
            DatabaseHandler.getHandlerObject();
        }).start();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
