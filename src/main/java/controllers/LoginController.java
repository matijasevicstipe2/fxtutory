package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.ini4j.Wini;
import org.xml.sax.SAXException;
import sample.Main;
import sample.UserCredentials;
import threads.GetRequestThread;
import threads.TcpServerThread;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class LoginController implements Initializable {
    @FXML
    private Label helloTitle;
    @FXML
    private MenuButton translateButton;
    @FXML
    private Label emailLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Button login;
    @FXML
    private Button newAcc;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setAllTexts();
        } catch (ParserConfigurationException | IOException | SAXException e) {

        }


    }
    @FXML
    public void hrvClick() throws ParserConfigurationException, IOException, SAXException {
        Main.setTranslate(true);
        setAllTexts();
    }
    @FXML
    public void engClick() throws ParserConfigurationException, IOException, SAXException {
        Main.setTranslate(false);
        setAllTexts();
    }
    public void setAllTexts() throws ParserConfigurationException, IOException, SAXException {
        helloTitle.setText(Main.readingXML(1,Main.translate));
        translateButton.setText(Main.readingXML(3,Main.translate));
        emailLabel.setText(Main.readingXML(4,Main.translate));
        passwordLabel.setText(Main.readingXML(5,Main.translate));
        login.setText(Main.readingXML(0,Main.translate));
        newAcc.setText(Main.readingXML(2,Main.translate));
    }
    @FXML
    public void transferToRegistration() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("registration2.fxml"));
        Scene scene = new Scene(parent,600,400);
        Main.getMainStage().setScene(scene);
    }
    @FXML
    public void acceptLogin() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IOException, ParserConfigurationException, SAXException, ExecutionException, InterruptedException {
        String hashedPass = Main.hashingPass(passwordField.getText());
        UserCredentials user = new UserCredentials(emailField.getText(),hashedPass);
        //thread3
        TcpServerThread tcpServerThread = new TcpServerThread(user);
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> future = executorService.submit(tcpServerThread);
        String validUser = future.get();
        executorService.shutdown();
        System.out.println(validUser);


        if(Objects.equals(validUser,"true")){
            String write = emailField.getText() + "&" + hashedPass;
            String pathTxt = "C:\\Users\\stipe\\ntp_projekt\\currentUser.txt";
            Main.writingTxtFile(write,pathTxt,false);

            //ini
            Main.setCurrentUsername(Main.getCurrentUser().getUsername());
            Main.setLastLogIn(String.valueOf(LocalDateTime.now()));
            if(Main.translate){
                Main.setCurrentLanguage("hrv");
            }else{
                Main.setCurrentLanguage("en");
            }
            Main.setTotalUsers(String.valueOf(Main.getAllUsersClient().size()));
            Main.setTotalAds(String.valueOf(Main.getAdsClient("http://localhost:8080/ads/all").size()));


            Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));
            Scene scene = new Scene(parent,600,400);
            Main.getMainStage().setScene(scene);
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(Main.readingXML(30,Main.translate));
            alert.setHeaderText(Main.readingXML(28,Main.translate));
            alert.setContentText(Main.readingXML(29,Main.translate));
            alert.showAndWait();
        }



    }
}
