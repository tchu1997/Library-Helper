package library.helper.ui.utility;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LibraryHelperUtility {
    public static final String ICON_IMAGE = "/utility/books.png";
    
    public static void setSceneIcon(Stage stage)
    {
        stage.getIcons().add(new Image(ICON_IMAGE));
    }
}
