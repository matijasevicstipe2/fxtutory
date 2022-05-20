package sample;

import com.google.common.hash.Hashing;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ini4j.Wini;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import threads.GetRequestThread;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Main extends Application {
    public static Boolean translate;
    private static Stage firstStage;

    public static String[] mockSortList = new String[]{
            "datumMinMax","datumMaxMin","cijenaMaxMin","cijenaMinMax"
    };
    public static Long adId;
    public static List<AdClient> ads;
    public static String currentUsername;
    public static String lastLogIn;
    public static String lastLogOut;
    public static String currentLanguage;
    public static String totalUsers;
    public static String totalAds;
    public static Boolean connection = false;

    public static String getCurrentLanguage() {
        return currentLanguage;
    }

    public static void setCurrentLanguage(String currentLanguage) {
        Main.currentLanguage = currentLanguage;
    }

    public static String getTotalUsers() {
        return totalUsers;
    }

    public static void setTotalUsers(String totalUsers) {
        Main.totalUsers = totalUsers;
    }

    public static String getTotalAds() {
        return totalAds;
    }

    public static void setTotalAds(String totalAds) {
        Main.totalAds = totalAds;
    }

    public static String getLastLogOut() {
        return lastLogOut;
    }

    public static void setLastLogOut(String lastLogOut) {
        Main.lastLogOut = lastLogOut;
    }

    public static String getLastLogIn() {
        return lastLogIn;
    }

    public static void setLastLogIn(String lastLogIn) {
        Main.lastLogIn = lastLogIn;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static void setCurrentUsername(String currentUsername) {
        Main.currentUsername = currentUsername;
    }

    public static List<AdClient> getAds() {
        return ads;
    }

    public static void setAds(List<AdClient> ads) {
        Main.ads = ads;
    }

    public static Long getAdId() {
        return adId;
    }

    public static void setAdId(Long adId) {
        Main.adId = adId;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        translate = false;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        firstStage = primaryStage;
        primaryStage.setTitle("Tutory");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();

    }


    public static String readingXML(int id, Boolean translate) throws ParserConfigurationException,
            IOException, SAXException {

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document dom = builder.parse(new File("languages.xml"));
        dom.normalizeDocument();

        Element root = dom.getDocumentElement();
        String tag = "";
        if(translate){
            tag = "hr";
        }else{
            tag = "en";
        }
        String wordExpression = root.getElementsByTagName(tag).item(id).getTextContent();
        return wordExpression;

    }

    public static void setTranslate(Boolean translate) {
        Main.translate = translate;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getMainStage(){
        return firstStage;
    }

    public static String hashingPass(String oldPass){
        String hashedPass = Hashing.sha256()
                .hashString(oldPass, StandardCharsets.UTF_8)
                .toString();
        return hashedPass;
    }

    /*public static String sendGET(String path){
        String result = "";
        try {

            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                //System.out.println(output);
                result = output;
            }


            conn.disconnect();


        } catch (IOException e) {

            e.printStackTrace();

        }
        return result;

    }*/


    /*public static void sendPOST(JSONObject regJson, String path) throws IOException {

        URL url = new URL (path);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        try(OutputStream os = con.getOutputStream()) {
            byte[] input =regJson.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }

    }*/
    /*public static boolean checkIfUserIsValid(UserCredentials user) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {

        String userCredentials = user.getEmail() +  "&" +  user.getPassword();
        SecretKey keyAES = generateKeyAES(128);
        IvParameterSpec iv = generateIv();

        //encryptData
        String encryptedUserCredentials = encryptAES(userCredentials, keyAES, iv);

        //toStringKeyAES
        String keyString = convertSecretKeyToString(keyAES);

        byte[] ivByte = iv.getIV();
        Files.write(Paths.get("C:\\Users\\stipe\\ntp_projekt\\iv.dat"), ivByte);

        //encryptKey
        String encryptedKey = encryptRSA(keyString);

        String hostname = "localhost";
        int port = 505;
        Boolean valid = false;

        try (Socket socket = new Socket(hostname, port)) {

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            writer.println(encryptedUserCredentials + "&" + encryptedKey);

            InputStream input = socket.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if(Objects.equals(line,"true")){
                    valid = true;
                }
            }


        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }

        return valid;

    }*/

    public static String encryptAES(String input, SecretKey keyAES, IvParameterSpec iv) throws NoSuchPaddingException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException {



        String algorithm = "AES/CBC/PKCS5Padding";

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, keyAES,iv);
        byte[] cipherBinary = cipher.doFinal(input.getBytes());
        String cipherText = Base64.getEncoder().encodeToString(cipherBinary);

        return cipherText;
    }

    public static String encryptRSA(String keyString) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        try (FileOutputStream fos = new FileOutputStream("C:\\Users\\stipe\\ntp_projekt\\private.key")) {
            fos.write(privateKey.getEncoded());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] secretMessageBytes = keyString.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
        String encodedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);
        return encodedMessage;
    }



    public static SecretKey generateKeyAES(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }


    public static String convertSecretKeyToString(SecretKey secretKey) {
        byte[] rawData = secretKey.getEncoded();
        String encodedKey = Base64.getEncoder().encodeToString(rawData);
        return encodedKey;
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static void writingTxtFile(String write, String path, Boolean append){
        try {
            FileWriter writer = new FileWriter(path, append);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            bufferedWriter.write(write);
            bufferedWriter.newLine();

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String readingTxtFile(String path){
        String user = "";

        try {
            FileReader reader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                user = line;
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return user;

    }

    public static List<AdClient> getAdsClient(String path) throws ExecutionException, InterruptedException {
        
        //thread1
        GetRequestThread getRequestThread = new GetRequestThread(path);
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> future = executorService.submit(getRequestThread);
        String result = future.get();
        executorService.shutdown();
        List<AdClient> ads = new ArrayList<>();
        if(!Objects.equals(result,"/")){
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = new JSONArray();
            try {
                jsonArray = (JSONArray) parser.parse(result);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for(int i = 0;i < jsonArray.size();i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                ads.add(new AdClient(
                        (Long) jsonObject.get("id"),
                        (String) jsonObject.get("title"),
                        (String) jsonObject.get("info"),
                        (Long) jsonObject.get("price"),
                        Date.valueOf((String) jsonObject.get("published")),
                        (Long) jsonObject.get("locationId"),
                        (Long) jsonObject.get("fieldId"),
                        (Long) jsonObject.get("userId")));

            }
        }
        return ads;
    }


    public static UserClient foundUserForAd(AdClient adClient) throws ExecutionException, InterruptedException {

        JSONParser parser = new JSONParser();
        UserClient foundUser = new UserClient();

        String pathForUser = "http://localhost:8080/users/user/" + adClient.getUserId();
        //thread2
        GetRequestThread getRequestThread = new GetRequestThread(pathForUser);
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> future = executorService.submit(getRequestThread);
        String foundUserResult = future.get();
        executorService.shutdown();


        JSONObject userJson = new JSONObject();
        try {
            userJson = (JSONObject) parser.parse(foundUserResult);
            foundUser = new UserClient(
                    (Long) userJson.get("id"),
                    (String) userJson.get("name"),
                    (String) userJson.get("lastName"),
                    (String) userJson.get("username"),
                    (String) userJson.get("password") ,
                    (String) userJson.get("email"),
                    Date.valueOf((String) userJson.get("dateOfBirth")),
                    (String) userJson.get("profilePicture"),
                    (String) userJson.get("contact"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return foundUser;
    }

    public static List<UserClient> getAllUsersClient() throws ExecutionException, InterruptedException {

        String path = "http://localhost:8080/users/all";

        //thread3
        GetRequestThread getRequestThread = new GetRequestThread(path);
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> future = executorService.submit(getRequestThread);
        String result = future.get();
        executorService.shutdown();

        JSONParser parser = new JSONParser();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = (JSONArray) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<UserClient> users = new ArrayList<>();

        for(int i = 0;i < jsonArray.size();i++){
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

            users.add(new UserClient(
                    (Long) jsonObject.get("id"),
                    (String) jsonObject.get("name"),
                    (String) jsonObject.get("lastName"),
                    (String) jsonObject.get("username"),
                    (String) jsonObject.get("email"),
                    (String) jsonObject.get("password"),
                    Date.valueOf((String) jsonObject.get("dateOfBirth")),
                    (String) jsonObject.get("profilePicture"),
                    (String) jsonObject.get("contact")));

        }
        return users;
    }

    public static String getLastLogIni() throws IOException {
        Wini ini = new Wini(new File("C:\\Users\\stipe\\ntp_projekt\\configtutory.ini"));
        String logout = ini.get("user", "lastLogOut");
        return logout;
    }

    public static void writeIniFile(String name,String logIn,
                                    String logOut,String lang,String tUsers,String tAds) throws IOException {
        Wini ini = new Wini(new File("C:\\Users\\stipe\\ntp_projekt\\configtutory.ini"));

        ini.put("user", "name", name);
        ini.put("user", "lastLogIn", logIn);
        if(logOut == null){
            ini.put("user", "lastLogOut", getLastLogIni());
        }else{
            ini.put("user", "lastLogOut", logOut);
        }
        ini.put("app", "language",lang );
        ini.put("app", "totalUsers", tUsers);
        ini.put("app", "totalAds", tAds);
        ini.store();
    }
    public static List<String> readIniFile() throws IOException {
        Wini ini = new Wini(new File("C:\\Users\\stipe\\ntp_projekt\\configtutory.ini"));

        List<String> stringList = new ArrayList<>();
        String username = ini.get("user", "name");
        stringList.add(username);
        String login = ini.get("user", "lastLogIn");
        stringList.add(login);
        String logout = ini.get("user", "lastLogOut");
        stringList.add(logout);
        String lang = ini.get("app", "language");
        stringList.add(lang);
        String tUsers = ini.get("app", "totalUsers");
        stringList.add(tUsers);
        String tAds = ini.get("app", "totalAds");
        stringList.add(tAds);

        return stringList;
    }

    public static UserClient getCurrentUser() throws ExecutionException, InterruptedException {
        UserClient user = new UserClient();
        String pathTxt = "C:\\Users\\stipe\\ntp_projekt\\currentUser.txt";
        String readTxt = Main.readingTxtFile(pathTxt);
        String[] array = readTxt.split("&");
        List<UserClient> users = Main.getAllUsersClient();
        for(UserClient userClient : users){

            if(Objects.equals(userClient.getEmail(),array[0])){
                user = userClient;
            }
        }

        return user;
    }

    public static List<FieldClient> getAllFields() throws ExecutionException, InterruptedException {
        String path = "http://localhost:8080/fields/all";

        //thread3
        GetRequestThread getRequestThread = new GetRequestThread(path);
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> future = executorService.submit(getRequestThread);
        String result = future.get();
        executorService.shutdown();

        JSONParser parser = new JSONParser();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = (JSONArray) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<FieldClient> fields = new ArrayList<>();

        for(int i = 0;i < jsonArray.size();i++){
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

            fields.add(new FieldClient(
                    (Long) jsonObject.get("id"),
                    (String) jsonObject.get("name")));

        }
        return fields;
    }
    public static List<LocationClient> getAllLocations() throws ExecutionException, InterruptedException {
        String path = "http://localhost:8080/locations/all";

        //thread3
        GetRequestThread getRequestThread = new GetRequestThread(path);
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> future = executorService.submit(getRequestThread);
        String result = future.get();
        executorService.shutdown();

        JSONParser parser = new JSONParser();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = (JSONArray) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<LocationClient> locations = new ArrayList<>();

        for(int i = 0;i < jsonArray.size();i++){
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

            locations.add(new LocationClient(
                    (Long) jsonObject.get("id"),
                    (String) jsonObject.get("name")));

        }
        return locations;
    }
    public static FieldClient getFieldById(Long id) throws ExecutionException, InterruptedException {
        JSONParser parser = new JSONParser();
        FieldClient foundField = new FieldClient();

        String pathForField = "http://localhost:8080/fields/field/" + id;
        //thread2
        GetRequestThread getRequestThread = new GetRequestThread(pathForField);
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> future = executorService.submit(getRequestThread);
        String foundFieldResult = future.get();
        executorService.shutdown();


        JSONObject fieldJson = new JSONObject();
        try {
            fieldJson = (JSONObject) parser.parse(foundFieldResult);
            foundField = new FieldClient(
                    (Long) fieldJson.get("id"),
                    (String) fieldJson.get("name"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return foundField;
    }
    public static LocationClient getLocationById(Long id) throws ExecutionException, InterruptedException {
        JSONParser parser = new JSONParser();
        LocationClient foundLocation = new LocationClient();

        String pathForLocation = "http://localhost:8080/locations/location/" + id;
        //thread2
        GetRequestThread getRequestThread = new GetRequestThread(pathForLocation);
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> future = executorService.submit(getRequestThread);
        String foundLocationResult = future.get();
        executorService.shutdown();


        JSONObject locationJson = new JSONObject();
        try {
            locationJson = (JSONObject) parser.parse(foundLocationResult);
            foundLocation = new LocationClient(
                    (Long)  locationJson.get("id"),
                    (String) locationJson.get("name"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return foundLocation;
    }

    public static List<AdClient> getUserAds() throws ExecutionException, InterruptedException {

        UserClient currentUser = getCurrentUser();
        List<AdClient> ads = getAdsClient("http://localhost:8080/ads/user/" + currentUser.getId());

        return ads;
    }


}
