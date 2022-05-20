package threads;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import static sample.Main.connection;

public class PostRequestThread implements Runnable{
    private JSONObject jsonObject;
    private String path;

    public PostRequestThread(JSONObject jsonObject, String path) {
        this.jsonObject = jsonObject;
        this.path = path;
    }

    @Override
    public synchronized void run() {
        try {
            if(connection == true){
                wait();
            }
            connection = true;
            URL url = new URL (path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            try(OutputStream os = con.getOutputStream()) {
                byte[] input =jsonObject.toString().getBytes("utf-8");
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
            connection = false;
            notifyAll();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
