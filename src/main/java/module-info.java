module com.cgvsu.protocurvefxapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.cgvsu.protocurvefxapp to javafx.fxml;
    exports com.cgvsu.protocurvefxapp;
    exports com.cgvsu.protocurvefxapp.draw_elements;
    opens com.cgvsu.protocurvefxapp.draw_elements to javafx.fxml;
}