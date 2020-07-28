package mainPackage.dataOperating;

import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TextData {
    private static final TextData instance = new TextData();

    public static TextData getInstance() { return instance; }
    // Save changes made in the text file, that are separated with enters
    public void saveFile(File file, String content) {
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file.getPath()))) {
                // Split text into single lines
                String[] separatedLines = content.split("\n");
                // Write each line from the array
                for(String string : separatedLines) {
                    writer.write(string);
                    writer.newLine();
                }
            } catch (IOException e) {
                showError("Cannot save file");
            }
        }
    public List<String> openFile(File file) {
        List<String> rows = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(file.getPath()))) {
            String row;
            // Get each row from file
            while((row = reader.readLine()) != null) {
                rows.add(row);
            }
        } catch (IOException e) {
            showError("Cannot open file");
        }
        return rows;
    }
    // Show dialog which informs user of error occured
    private void showError(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
