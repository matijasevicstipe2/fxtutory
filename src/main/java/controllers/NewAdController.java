package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.checkerframework.checker.initialization.qual.FBCBottom;
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

public class NewAdController implements Initializable {
    @FXML
    private Label label;
    @FXML
    private Label title;
    @FXML
    private Label field;
    @FXML
    private Label info;
    @FXML
    private Label location;
    @FXML
    private Label price;
    @FXML
    private Button createButton;
    @FXML
    private TextField titleField;
    @FXML
    private ListView<String> fieldList;
    @FXML
    private TextArea infoField;
    @FXML
    private ListView<String> locationList;
    @FXML
    private TextField priceField;
    @FXML
    private Button goBackButton;

    private List<FieldClient> fields;
    private List<LocationClient> locations;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            setAllTexts();
        } catch (ParserConfigurationException | IOException | SAXException e) {

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
        fieldList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

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
        label.setText(Main.readingXML(20,Main.translate));
        title.setText(Main.readingXML(21,Main.translate));
        field.setText(Main.readingXML(22,Main.translate));
        info.setText(Main.readingXML(23,Main.translate));
        location.setText(Main.readingXML(24,Main.translate));
        price.setText(Main.readingXML(25,Main.translate));
        createButton.setText(Main.readingXML(26,Main.translate));
        goBackButton.setText(Main.readingXML(27,Main.translate));
    }

    @FXML
    public void createNewAd() throws IOException {

        JSONObject newAdJson =  new JSONObject();
        newAdJson.put("title",titleField.getText());
        FieldClient fieldClient = fields.stream()
                .filter(f ->
                        Objects.equals(f.getName(),fieldList.getSelectionModel().getSelectedItem())).findFirst().get();
        newAdJson.put("field", fieldClient.getId().toString());
        newAdJson.put("info", infoField.getText());

        LocationClient locationClient = locations.stream()
                .filter(l ->
                        Objects.equals(l.getName(),locationList.getSelectionModel().getSelectedItem())).findFirst().get();
        newAdJson.put("location",locationClient.getId().toString());
        newAdJson.put("price",priceField.getText());

        String pathTxt = "C:\\Users\\stipe\\ntp_projekt\\currentUser.txt";
        String user = Main.readingTxtFile(pathTxt);
        newAdJson.put("user",user);

        String path = "http://localhost:8080/ads/createAd";
        //Main.sendPOST(newAdJson,path);
        PostRequestThread postRequestThread = new PostRequestThread(newAdJson,path);
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(postRequestThread);
        executorService.shutdown();
        goBack();
    }

    @FXML
    public void goBack() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));
        Scene scene = new Scene(parent, 600, 400);
        Main.getMainStage().setScene(scene);
    }
}
