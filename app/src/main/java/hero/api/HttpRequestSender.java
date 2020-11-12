package hero.api;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestSender extends AsyncTask<Object, Object, String> {

    private static final int TIMEOUT = 6000;
    String method;
    String url;
    String sendingData;
    DataCallback dataCallback;

    public HttpRequestSender(String method, String url, String sendingData, DataCallback dataCallback) {
        this.method = method;
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
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);
            if (!method.equals("GET")) {
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestMethod(method);
                writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                writer.write(sendingData);
                writer.flush();
            }
            connection.connect();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            receivingData = reader.readLine();

        } catch (Exception e){
            try { this.dataCallback.onDataReceiving(null); }
            catch (Exception ex){ Log.d("error", e.getMessage()); }
            Log.e("error", e.getMessage());

        } finally {
            try{
                if (writer != null) writer.close();
                if (reader != null) reader.close();
                if (connection != null) connection.disconnect();
            } catch (Exception ex){ Log.e("error", ex.getMessage()); }
        }

        return receivingData;

    }

    @Override
    protected void onPostExecute(String result) {

        super.onPostExecute(result);

        try {
            JSONObject jsonObj = new JSONObject(result);
            if (jsonObj.getInt("code") >= 400) this.dataCallback.onDataReceiving(null);
            else this.dataCallback.onDataReceiving(jsonObj);

        } catch (Exception e){
            Log.e("error", e.getMessage());
            try { this.dataCallback.onDataReceiving(null); }
            catch (Exception ex){ Log.e("error", e.getMessage()); }
        }

    }

}

