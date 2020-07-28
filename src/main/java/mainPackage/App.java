package mainPackage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class App extends Application {
    // Variables used for make window draggable without Windows default frame
    double XOffset, YOffset;
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("mainWindow.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        primaryStage.setTitle("Text Editor");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setOpacity(0.92);
        // Set draggable window area used to change position on the screen
        controller.getMenuBar().setOnMousePressed(mouseEvent -> {
            XOffset = mouseEvent.getSceneX();
            YOffset = mouseEvent.getSceneY();
        });
        controller.getMenuBar().setOnMouseDragged(mouseEvent -> {
            primaryStage.setX(mouseEvent.getScreenX() - XOffset);
            primaryStage.setY(mouseEvent.getScreenY() - YOffset);
        });
        // Ask for save if working on opened file
        primaryStage.setOnCloseRequest(windowEvent -> {
            controller.handleExitMenuItem();
            windowEvent.consume();
        });
        primaryStage.getIcons().add(new Image(getClass().getResource("icon.png").toExternalForm()));
        primaryStage.setScene(new Scene(root, 900, 575));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}