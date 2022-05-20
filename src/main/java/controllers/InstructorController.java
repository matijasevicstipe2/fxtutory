package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import org.xml.sax.SAXException;
import sample.AdClient;
import sample.Main;
import sample.UserClient;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class InstructorController implements Initializable {

    @FXML
    private Label title;
    @FXML
    private Label label;
    @FXML
    private Button returnButton;
    @FXML
    private Button create;
    @FXML
    private ListView<String> userAds;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setAllTexts();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        List<AdClient> ads = null;
        try {
            ads = Main.getUserAds();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        List<String> stringList = new ArrayList<>();

        for(AdClient ad : ads){
            stringList.add(ad.getId() + "#  " + ad.getTitle() + "   " + ad.getPublished());
        }
        ObservableList<String> observableList = FXCollections.observableList(stringList);
        userAds.setItems(observableList);
        userAds.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {
                    System.out.println("k");
                }
            }
        });

    }

    public void setAllTexts() throws ParserConfigurationException, IOException, SAXException {
        title.setText(Main.readingXML(53,Main.translate));
        label.setText(Main.readingXML(54,Main.translate));
        create.setText(Main.readingXML(26,Main.translate));
        returnButton.setText(Main.readingXML(37,Main.translate));
    }

    @FXML
    public void createAd() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("newAd.fxml"));
        Scene scene = new Scene(parent,600,400);
        Main.getMainStage().setScene(scene);
    }

    @FXML
    public void goBack() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));
        Scene scene = new Scene(parent,600,400);
        Main.getMainStage().setScene(scene);
    }
}
