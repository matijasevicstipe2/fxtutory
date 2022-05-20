package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.CheckComboBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;
import sample.AdClient;
import sample.Main;
import sample.UserClient;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class SearchingController implements Initializable {

    @FXML
    private Label heading;
    @FXML
    private Label searchByTitleLabel;
    @FXML
    private Label sortLabel;
    @FXML
    private TextField searchByTitle;
    @FXML
    private Button search;
    @FXML
    private ListView<String> sort;
    @FXML
    private ListView<String> adsView;
    @FXML
    private Button goBackButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            setAllTexts();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

        sort.getItems().addAll(Main.mockSortList);
        sort.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        adsView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


    }

    @FXML
    public void searchAds() throws ExecutionException, InterruptedException {

        List<AdClient> ads = findSuitableAds();
        UserClient foundUser;
        List<String> stringList = new ArrayList<>();

        for(AdClient adClient: ads){
            System.out.println(adClient.getPublished());
            foundUser = Main.foundUserForAd(adClient);

            try {
                stringList.add(adClient.getId() + "#  " +
                        Main.readingXML(31,Main.translate) + " " + foundUser.getUsername()
                        + "   " + adClient.getPublished() + "      " + adClient.getTitle());

            } catch (ParserConfigurationException | IOException | SAXException e) {
                e.printStackTrace();
            }

        }

        ObservableList<String> observableList = FXCollections.observableList(stringList);
        adsView.setItems(observableList);

        adsView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {
                    String currentItemSelected = adsView.getSelectionModel()
                            .getSelectedItem();
                    String[] array = currentItemSelected.split("#");
                    Long id = Long.valueOf(array[0]);
                    System.out.println(id);
                    Main.setAdId(id);
                    Main.setAds(ads);
                    Parent parent = null;
                    try {
                        parent = FXMLLoader.load(getClass().getClassLoader().getResource("selectedAd.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene scene = new Scene(parent,600,400);
                    Main.getMainStage().setScene(scene);
                }
            }
        });

    }

    @FXML
    public void goBack() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("learnerMode.fxml"));
        Scene scene = new Scene(parent,600,400);
        Main.getMainStage().setScene(scene);
    }

    public List<AdClient> findSuitableAds() throws ExecutionException, InterruptedException {


        ObservableList<String> selectedSorts;
        selectedSorts = sort.getSelectionModel().getSelectedItems();

        UserClient user = Main.getCurrentUser();
        String path = "http://localhost:8080/ads/criteria/" + user.getId();
        List<AdClient> ads = Main.getAdsClient(path);
        System.out.println(ads.size() + "aadd");
        List<AdClient> adsAfterTitleSearch = new ArrayList<>();
        List<AdClient> adsAfterSort= new ArrayList<>();

        //pretraga po naslovu
        for(AdClient adClient : ads){
            if(adClient.getTitle().toLowerCase().contains(searchByTitle.getText().toLowerCase())){
                adsAfterTitleSearch.add(adClient);
            }
        }



        if(selectedSorts.isEmpty()){
            adsAfterSort.addAll(adsAfterTitleSearch);
        }else{
            //sortiranje
            for(int i = 0;i < selectedSorts.size();i++){
                if(selectedSorts.get(i) == "datumMinMax"){
                    adsAfterTitleSearch = adsAfterTitleSearch
                            .stream()
                            .sorted(Comparator.comparing(AdClient::getPublished))
                            .collect(Collectors.toList());
                }else if(selectedSorts.get(i) == "datumMaxMin"){
                    adsAfterTitleSearch = adsAfterTitleSearch
                            .stream()
                            .sorted(Comparator.comparing(AdClient::getPublished).reversed())
                            .collect(Collectors.toList());
                }else if(selectedSorts.get(i) == "cijenaMinMax"){
                    adsAfterTitleSearch = adsAfterTitleSearch
                            .stream()
                            .sorted(Comparator.comparing(AdClient::getPrice)).collect(Collectors.toList());
                }else if(selectedSorts.get(i) == "cijenaMaxMin"){
                    adsAfterTitleSearch = adsAfterTitleSearch
                            .stream()
                            .sorted(Comparator.comparing(AdClient::getPrice).reversed()).collect(Collectors.toList());
                }
            }
            adsAfterSort.addAll(adsAfterTitleSearch);
        }

        return adsAfterSort;


    }

    public void setAllTexts() throws ParserConfigurationException, IOException, SAXException {
        heading.setText(Main.readingXML(32,Main.translate));
        searchByTitleLabel.setText(Main.readingXML(33,Main.translate));
        sortLabel.setText(Main.readingXML(35,Main.translate));
        search.setText(Main.readingXML(36,Main.translate));
        goBackButton.setText(Main.readingXML(37,Main.translate));

    }



}
