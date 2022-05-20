package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.xml.sax.SAXException;
import sample.Main;


import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML
    private Label settings;
    @FXML
    private Label user;
    @FXML
    private Label app;
    @FXML
    private Button goBackButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            settings.setText(Main.readingXML(40,Main.translate));
            goBackButton.setText(Main.readingXML(37,Main.translate));
            List<String> iniStrings = Main.readIniFile();

            user.setText(Main.readingXML(41,Main.translate) + " " + iniStrings.get(0) + "\n"
                    + Main.readingXML(42,Main.translate) + " " + iniStrings.get(1) + "\n"
                    + Main.readingXML(43,Main.translate) + " " + iniStrings.get(2) + "\n");

            app.setText(Main.readingXML(44,Main.translate) + " " + iniStrings.get(3) + "\n"
                    + Main.readingXML(45,Main.translate) + " " + iniStrings.get(4) + "\n"
                    + Main.readingXML(46,Main.translate) + " " + iniStrings.get(5) + "\n");

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goBack() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));
        Scene scene = new Scene(parent,600,400);
        Main.getMainStage().setScene(scene);
    }
}
