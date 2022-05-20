package threads;

import sample.Main;
import sample.UserCredentials;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.Callable;
import static sample.Main.connection;
public class TcpServerThread implements Callable<String> {
    private UserCredentials user;

    public TcpServerThread(UserCredentials user) {
        this.user = user;
    }

    @Override
    public synchronized String call() throws Exception {
        if(connection == true){
            wait();
        }
        connection = true;
        String userCredentials = user.getEmail() +  "&" +  user.getPassword();
        SecretKey keyAES = Main.generateKeyAES(128);
        IvParameterSpec iv = Main.generateIv();

        //encryptData
        String encryptedUserCredentials = Main.encryptAES(userCredentials, keyAES, iv);

        //toStringKeyAES
        String keyString = Main.convertSecretKeyToString(keyAES);

        byte[] ivByte = iv.getIV();
        Files.write(Paths.get("C:\\Users\\stipe\\ntp_projekt\\iv.dat"), ivByte);

        //encryptKey
        String encryptedKey = Main.encryptRSA(keyString);

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


        connection = false;
        notifyAll();

        return valid.toString();

    }
}
