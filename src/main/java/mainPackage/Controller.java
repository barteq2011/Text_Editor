package mainPackage;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import mainPackage.dataOperating.TextData;
import java.io.File;
import java.util.List;
import java.util.Optional;

public class Controller {
    @FXML
    private MenuBar menuBar;
    @FXML
    private CheckMenuItem wordWrapMenuItem;
    @FXML
    private TextArea mainTextArea;
    @FXML
    private BorderPane mainBorderPane;
    private File actualFile;
    // Key combination used for saving changes made in file easier
    private final KeyCombination closingCombination = new KeyCodeCombination(KeyCode.S,
            KeyCombination.CONTROL_DOWN);

    @FXML
    public void initialize() {
        // Handle Ctrl+S key combination by saving changes made to file
        mainBorderPane.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
                if(closingCombination.match(keyEvent))
                    handleSaveMenuItem();
            });
    }

    @FXML
    public void handleNewMenuItem() {
        // If user passed the save dialog window, create new document
        if(askForSave())
            mainTextArea.setText("");
    }
    @FXML
    public void handleOpenMenuItem() {
        // If user passed save dialog window, begin choosing and opening file operation
        if(askForSave()) {
            FileChooser chooser = new FileChooser();
            // Editor supports only text files (.txt)
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
                    "Text files", "*.txt");
            chooser.getExtensionFilters().add(filter);
            File file = chooser.showOpenDialog(mainBorderPane.getScene().getWindow());
            // If user selected file, open it
            if (file != null) {
                // Clear text area for new content
                mainTextArea.setText("");
                // Rows of text got from file
                List<String> dataRows = TextData.getInstance().openFile(file);
                // Fill text area with rows got from file
                for (String row : dataRows) {
                    mainTextArea.setText(mainTextArea.getText() + row + "\n");
                }
                // The file the user is working on now
                setActualFile(file);
            }
        }
    }
    @FXML
    public void handleSaveMenuItem() {
        // If user is working on file, save actual changes in it
        if(actualFile!=null)
           TextData.getInstance().saveFile(actualFile, getUserText());
        // If not, open save dialog window for creating new file
        else
            showSaveFileDialog();
    }
    @FXML
    public void handleSaveAsMenuItem() {
        showSaveFileDialog();
    }
    @FXML
    public void handleExitMenuItem() {
        // If user passed save file dialog, exit app
        if(askForSave())
            Platform.exit();
    }
    @FXML
    public void handleWordWrapMenuItem() {
        // Menu item used for on and off wrapping rows
        if(wordWrapMenuItem.isSelected()) {
            mainTextArea.setWrapText(true);
        } else
            mainTextArea.setWrapText(false);
    }
    @FXML
    public void handleAboutMenuItem() {
        // Open window which will display info about author
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About app");
        alert.setContentText("Simple text editor by Bartosz Bartosik");
        alert.setHeaderText(null);
        alert.show();
    }

    private boolean askForSave() {
        // Flag used for declaring that user didn't cancel saving operation
        boolean flag = true;
        if((actualFile!=null) || !mainTextArea.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.initOwner(mainBorderPane.getScene().getWindow());
            alert.setTitle("Confirm");
            alert.setHeaderText(null);
            alert.setContentText("Do you want to save actual file?");
            alert.getButtonTypes().add(ButtonType.YES);
            alert.getButtonTypes().add(ButtonType.NO);
            alert.getButtonTypes().add(ButtonType.CANCEL);
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.YES) {
                // If user is working on file, save changes in it, if not - open dialog to create new file
                if(actualFile != null) {
                    TextData.getInstance().saveFile(actualFile, getUserText());
                } else
                    showSaveFileDialog();
                // If user canceled operation, do nothing
            } else if(result.isPresent() && result.get() == ButtonType.CANCEL) {
                flag = false;
            }
        }
        return flag;
    }
    private void showSaveFileDialog() {
        String text = getUserText();
        FileChooser chooser = new FileChooser();
        // Editor supports only text files (.txt)
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Text files", "*.txt");
        chooser.getExtensionFilters().add(filter);
        File file = chooser.showSaveDialog(mainBorderPane.getScene().getWindow());
        // If user selected file, save it
        if(file!=null) {
            TextData.getInstance().saveFile(file, text);
            setActualFile(file);
        }
    }
    // Menu bar access for main method, used to create draggable area
    public MenuBar getMenuBar() { return menuBar; }
    // Set the file the user will be working on
    private void setActualFile(File file) { actualFile = file; }
    // Get text from text area
    private String getUserText() { return mainTextArea.getText(); }
}
