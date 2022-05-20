package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.xml.sax.SAXException;
import sample.Main;
import sample.UserClient;


import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class ProfileController implements Initializable {
    @FXML
    private Label profileTitle;
    @FXML
    private Label helloMessage;
    @FXML
    private Label firstName;
    @FXML
    private Label lastName;
    @FXML
    private Label birth;
    @FXML
    private Label contact;
    @FXML
    private ImageView profilePic;
    @FXML
    private Button button;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        UserClient user = null;
        try {
            user = Main.getCurrentUser();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }


        try {
            profileTitle.setText(Main.readingXML(38,Main.translate));
            helloMessage.setText(Main.readingXML(39,Main.translate) + " " + user.getUsername() + "!");
            firstName.setText(user.getName());
            lastName.setText(user.getLastName());
            birth.setText(user.getDateOfBirth().toString());
            contact.setText(user.getEmail());
            button.setText(Main.readingXML(37,Main.translate));

            byte[] decodedPicture = Base64.getDecoder().decode(user.getProfilePicture());
            InputStream is = new ByteArrayInputStream(decodedPicture);
            BufferedImage bi = ImageIO.read(is);

            ImageIO.write(bi, "png", new File("C:\\Users\\stipe\\ntp_projekt\\profile.png"));
            Image image = new Image("C:\\Users\\stipe\\ntp_projekt\\profile.png");
            profilePic.setImage(image);

        } catch (ParserConfigurationException | IOException | SAXException e) {
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
