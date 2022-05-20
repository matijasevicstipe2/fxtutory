package threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import static sample.Main.connection;

public class GetRequestThread implements Callable<String> {

    private String path;

    public GetRequestThread(String path) {
        this.path = path;
    }

    @Override
    public synchronized String call() throws Exception {
        String result = "";
        try {
            if(connection == true){
                wait();
            }
            connection = true;

            try {

                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() != 200) {
                    if(conn.getResponseCode() != 204){
                        throw new RuntimeException("Failed : HTTP error code : "
                                + conn.getResponseCode());
                    }

                }

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                String output;
                System.out.println("Output from Server .... \n");
                if(conn.getResponseCode() == 204){
                    result = "/";
                }
                else{
                    while ((output = br.readLine()) != null) {
                        // System.out.println(output);
                        result = output;
                    }
                }

                conn.disconnect();


            } catch (IOException e) {

                e.printStackTrace();

            }

            connection = false;
            notifyAll();


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
