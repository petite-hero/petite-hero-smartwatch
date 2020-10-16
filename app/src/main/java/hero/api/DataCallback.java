package hero.api;

import org.json.JSONObject;

public interface DataCallback {

    void onDataReceiving(JSONObject data) throws Exception;

}