package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.xml.sax.SAXException;
import sample.Main;


import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Button profile;
    @FXML
    private Button settings;
    @FXML
    private Button logOutButton;
    @FXML
    private Button learner;
    @FXML
    private Button instructor;
    @FXML
    private VBox vBox;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            setAllTexts();
        } catch (ParserConfigurationException | IOException | SAXException e) {

        }
        vBox.setBackground(
                new Background(

                        new BackgroundFill(Color.rgb(153, 153, 153), CornerRadii.EMPTY, Insets.EMPTY)));
        try {
            Main.writeIniFile(Main.getCurrentUsername(),Main.getLastLogIn(),
                    Main.getLastLogOut(),Main.getCurrentLanguage(),Main.getTotalUsers(),Main.getTotalAds());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void setAllTexts() throws ParserConfigurationException, IOException, SAXException {
        profile.setText(Main.readingXML(16,Main.translate));
        settings.setText(Main.readingXML(17,Main.translate));
        logOutButton.setText(Main.readingXML(18,Main.translate));
        learner.setText(Main.readingXML(49,Main.translate));
        instructor.setText(Main.readingXML(48,Main.translate));

    }
    @FXML
    public void instructorMode() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("instructorMode.fxml"));
        Scene scene = new Scene(parent,600,400);
        Main.getMainStage().setScene(scene);
    }

    @FXML
    public void learnerMode() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("learnerMode.fxml"));
        Scene scene = new Scene(parent,600,400);
        Main.getMainStage().setScene(scene);
    }

    @FXML
    public void logOut() throws IOException {
        Main.setLastLogOut(String.valueOf(LocalDateTime.now()));
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        Scene scene = new Scene(parent,600,400);
        Main.getMainStage().setScene(scene);
    }

    @FXML
    public void showUserProfile() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("profile.fxml"));
        Scene scene = new Scene(parent,600,400);
        Main.getMainStage().setScene(scene);
    }

    @FXML
    public void showAppSettings() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("settings.fxml"));
        Scene scene = new Scene(parent,600,400);
        Main.getMainStage().setScene(scene);
    }

}
