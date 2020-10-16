package hero.api;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GETRequestSender extends AsyncTask<Object, Object, String> {

    String url;
    DataCallback dataCallback;

    public GETRequestSender(String url, DataCallback dataCallback) {
        this.url = url;
        this.dataCallback = dataCallback;
    }

    @Override
    protected String doInBackground(Object[] objects) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String json = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            json = reader.readLine();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try{
                if (reader != null) reader.close();
                if (connection != null) connection.disconnect();
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return json;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
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
