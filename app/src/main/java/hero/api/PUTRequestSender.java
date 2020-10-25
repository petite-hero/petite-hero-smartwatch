package hero.api;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class PUTRequestSender extends AsyncTask<Object, Object, String> {

    String url;
    String sendingData;
    DataCallback dataCallback;

    public PUTRequestSender(String url, String sendingData, DataCallback dataCallback) {
        this.url = url;
        this.sendingData = sendingData;
        this.dataCallback = dataCallback;
    }

    @Override
    protected String doInBackground(Object[] objects) {
        HttpURLConnection connection = null;
        BufferedWriter writer = null;
        BufferedReader reader = null;
        String receivingData = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("PUT");
            writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            writer.write(sendingData);
            writer.flush();
            connection.connect();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            receivingData = reader.readLine();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try{
                if (writer != null) writer.close();
                if (reader != null) reader.close();
                if (connection != null) connection.disconnect();
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return receivingData;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (this.dataCallback == null) return;
        try {
            JSONObject jsonObj = new JSONObject(result);
            this.dataCallback.onDataReceiving(jsonObj);
        } catch (Exception e){
            e.printStackTrace();
            try {
                this.dataCallback.onDataReceiving(null);
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

}
