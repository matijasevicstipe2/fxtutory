package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.xml.sax.SAXException;
import sample.Main;
import threads.PostRequestThread;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegistrationController implements Initializable {
    @FXML
    private Label title;
    @FXML
    private Label name;
    @FXML
    private Label lastname;
    @FXML
    private Label username;
    @FXML
    private Label email;
    @FXML
    private Label pass;
    @FXML
    private Label date;
    @FXML
    private Button registrationB;
    @FXML
    private Label profilePicture;
    @FXML
    private Label pictureChosen;
    @FXML
    private Button choosePicture;
    @FXML
    private TextField nameField;
    @FXML
    private TextField lastnameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField contactField;
    @FXML
    private Label contact;

    private String imgFilePath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setAllTexts();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

    }

    public void setAllTexts() throws ParserConfigurationException, IOException, SAXException {
        title.setText(Main.readingXML(10,Main.translate));
        name.setText(Main.readingXML(6,Main.translate));
        lastname.setText(Main.readingXML(11,Main.translate));
        username.setText(Main.readingXML(7,Main.translate));
        email.setText(Main.readingXML(4,Main.translate));
        pass.setText(Main.readingXML(5,Main.translate));
        date.setText(Main.readingXML(8,Main.translate));
        registrationB.setText(Main.readingXML(9,Main.translate));
        profilePicture.setText(Main.readingXML(12,Main.translate));
        pictureChosen.setText(Main.readingXML(14,Main.translate));
        choosePicture.setText(Main.readingXML(15,Main.translate));
        contact.setText(Main.readingXML(47,Main.translate));
    }
    @FXML
    public void choosingPicture() throws IOException, ParserConfigurationException, SAXException {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        System.out.println(file.getAbsolutePath());
        imgFilePath = file.getAbsolutePath();
        pictureChosen.setText(Main.readingXML(13,Main.translate));

        /*InputStream is = new FileInputStream(imgFilePath);
        Image image = (Image) ImageIO.read(is);*/
    }
    @FXML
    public void acceptRegistration() throws IOException {
        String hashedPassword;
        JSONObject registrationJson = new JSONObject();
        registrationJson.put("name",nameField.getCharacters().toString());
        registrationJson.put("lastName",lastnameField.getCharacters().toString());
        registrationJson.put("username",usernameField.getCharacters().toString());
        registrationJson.put("email",emailField.getCharacters().toString());

        hashedPassword = Main.hashingPass(passwordField.getText());

        registrationJson.put("password", hashedPassword);
        registrationJson.put("dateOfBirth",datePicker.getValue().toString());
        registrationJson.put("profilePictureFilePath",imgFilePath);
        registrationJson.put("contact",contactField.getCharacters().toString());

        System.out.println(registrationJson);
        String path = "http://localhost:8080/users/createUser";

        //Main.sendPOST(registrationJson,path);
        PostRequestThread postRequestThread = new PostRequestThread(registrationJson,path);
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(postRequestThread);
        executorService.shutdown();

        //txt file - pisanje
        String write = new String(emailField.getCharacters().toString() + "&" + hashedPassword);
        String pathTxt = "C:\\Users\\stipe\\ntp_projekt\\userCheck.txt";
        Main.writingTxtFile(write,pathTxt,true);

        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        Scene scene = new Scene(parent,600,400);
        Main.getMainStage().setScene(scene);


    }
}
