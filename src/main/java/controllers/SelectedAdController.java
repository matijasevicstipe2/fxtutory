package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.checkerframework.checker.units.qual.A;
import org.xml.sax.SAXException;
import sample.AdClient;
import sample.FieldClient;
import sample.LocationClient;
import sample.Main;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class SelectedAdController implements Initializable {

    @FXML
    private Label title;
    @FXML
    private Label keywords;
    @FXML
    private Label info;
    @FXML
    private Label area;
    @FXML
    private Label price;
    @FXML
    private Button goBackButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        AdClient adFound = new AdClient();
        for(AdClient adClient : Main.getAds()){
            if(Objects.equals(adClient.getId(),Main.getAdId())){
                adFound = adClient;
            }
        }

        LocationClient location = new LocationClient();
        FieldClient field = new FieldClient();
        try {
            location = Main.getLocationById(adFound.getLocationId());
            field = Main.getFieldById(adFound.getFieldId());

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        try {
            title.setText(adFound.getTitle());
            keywords.setText(Main.readingXML(22,Main.translate) + " " + field.getName());
            info.setText(Main.readingXML(23,Main.translate) + " " + adFound.getInfo());
            area.setText(Main.readingXML(24,Main.translate) + " " + location.getName());
            price.setText(Main.readingXML(25,Main.translate) + " " + adFound.getPrice());
            goBackButton.setText(Main.readingXML(37,Main.translate));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goBack() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("searching.fxml"));
        Scene scene = new Scene(parent,600,400);
        Main.getMainStage().setScene(scene);
    }
}
