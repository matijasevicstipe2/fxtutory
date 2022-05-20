package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import org.json.simple.JSONObject;
import org.xml.sax.SAXException;
import sample.FieldClient;
import sample.LocationClient;
import sample.Main;
import threads.PostRequestThread;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LearnerController implements Initializable {
    @FXML
    private Label title;
    @FXML
    private Label field;
    @FXML
    private Label location;
    @FXML
    private Button criteria;
    @FXML
    private Button search;
    @FXML
    private Button returnButton;
    @FXML
    private ListView<String> fieldList;
    @FXML
    private ListView<String> locationList;

    private List<FieldClient> fields;
    private List<LocationClient> locations;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setAllTexts();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        //fields
        try {
            fields = Main.getAllFields();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        List<String> fieldNames = new ArrayList<>();
        for (FieldClient fieldClient : fields){
            fieldNames.add(fieldClient.getName());
        }

        fieldList.getItems().addAll(fieldNames);
        fieldList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //locations
        try {
            locations = Main.getAllLocations();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        List<String> locationNames = new ArrayList<>();
        for (LocationClient locationClient: locations){
            locationNames.add(locationClient.getName());
        }

        locationList.getItems().addAll(locationNames);
        locationList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public void setAllTexts() throws ParserConfigurationException, IOException, SAXException {
        title.setText(Main.readingXML(49,Main.translate));
        field.setText(Main.readingXML(50,Main.translate));
        location.setText(Main.readingXML(51,Main.translate));
        criteria.setText(Main.readingXML(52,Main.translate));
        search.setText(Main.readingXML(32,Main.translate));
        returnButton.setText(Main.readingXML(37,Main.translate));
    }
    @FXML
    public void searchAds() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("searching.fxml"));
        Scene scene = new Scene(parent,600,400);
        Main.getMainStage().setScene(scene);
    }
    @FXML
    public void goBack() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));
        Scene scene = new Scene(parent,600,400);
        Main.getMainStage().setScene(scene);
    }

    @FXML
    public void setCriteria(){
        JSONObject json = new JSONObject();

        String pathTxt = "C:\\Users\\stipe\\ntp_projekt\\currentUser.txt";
        String user = Main.readingTxtFile(pathTxt);
        json.put("user",user);
        json.put("fields",fieldList.getSelectionModel().getSelectedItems());
        json.put("location", locationList.getSelectionModel().getSelectedItem());
        System.out.println(json);

        String path = "http://localhost:8080/ads/demand";
        PostRequestThread postRequestThread = new PostRequestThread(json,path);
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(postRequestThread);
        executorService.shutdown();
    }
}
