module mainPackage {
    requires javafx.controls;
    requires javafx.fxml;

    opens mainPackage to javafx.fxml;
    exports mainPackage;
}