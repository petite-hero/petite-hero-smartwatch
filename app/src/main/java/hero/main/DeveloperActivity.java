package hero.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import hero.api.DataCallback;
import hero.api.HttpRequestSender;
import hero.service.FCMService;
import hero.service.LocationService;

public class DeveloperActivity extends Activity {

    EditText edtChildId;
//    TextView txtLocationStatus;
    TextView txtConfigInfo;
    SharedPreferences ref;
    FCMService fcmService;

    private static final String CHILD_ID = "3";
    private static final String IP_PORT = "http://192.168.1.5:8080";
    private static final int INTERVAL = 5000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        edtChildId = findViewById(R.id.edtChildId);
//        txtLocationStatus = findViewById(R.id.txtLocationStatus);
        txtConfigInfo = findViewById(R.id.txtConfigInfo);
        fcmService = new FCMService();
        ref = PreferenceManager.getDefaultSharedPreferences(this);

        fetchConfig();

        // test
//        setDefaultConfig(null);

    }

    // ------------------- CONFIG SETTING -------------------

    public void setIP(View view){
        SharedPreferences.Editor refEditor = ref.edit();
        refEditor.putString("ip_port", "http://" + edtChildId.getText().toString() + ":8080");
        refEditor.apply();
        Toast.makeText(this, "IP set as " + ref.getString("ip_port", null), Toast.LENGTH_LONG).show();
        fetchConfig();
    }

    public void setChildId(View view){
        SharedPreferences.Editor refEditor = ref.edit();
        refEditor.putString("child_id", edtChildId.getText().toString());
        refEditor.apply();
        Toast.makeText(this, "Child ID set as " + ref.getString("child_id", null), Toast.LENGTH_LONG).show();
        fetchConfig();
    }

    public void scanServerIP(View view){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    public void setDefaultConfig(View view){
        SharedPreferences.Editor refEditor = ref.edit();
        refEditor.putString("child_id", CHILD_ID);
        refEditor.putString("ip_port", IP_PORT);
        refEditor.putInt("report_interval", INTERVAL);
        refEditor.apply();
    }

    // ------------------- LOCATION ACTIONS -------------------

    public void startLocationService(View view){
        if (!LocationService.isRunning) {
            Intent locationIntent = new Intent(this, LocationService.class);
            startService(locationIntent);
        }
        LocationService.isRunning = true;
//        txtLocationStatus.setText("Location service started");
    }

    public void stopLocationService(View view){
        LocationService.isRunning = false;
//        txtLocationStatus.setText("Location service stopping requested. Wait " + ref.getInt("report_interval", -1)/1000 + " secs");
    }

    public void sendLocationWithStatus(View view){
        JSONObject locationJsonObj = new JSONObject();
        try {
            locationJsonObj.put("child", ref.getString("child_id", null))
                .put("latitude", 10.8414846)
                .put("longitude", 106.8100464)
                .put("provider", "test")
                .put("status", view.getTag().toString().equals("safe"))
                .put("time", new java.util.Date().getTime());
            Log.d("test", view.getTag().toString()+" location sent");
        } catch (JSONException e){
            e.printStackTrace();
        }
        new HttpRequestSender("POST", ref.getString("ip_port", null) + "/location/current-location/false", locationJsonObj.toString(),
            new DataCallback() {
                @Override
                public void onDataReceiving(JSONObject data) throws Exception {
                    Log.d("test", "Sent location with status | Response: " + data.toString());
                }
            }
        ).execute();
    }

    public void getSafeZones(View view){
//        new HttpRequestSender("GET", ref.getString("ip_port", null) + "/location/list/" +
//                                 ref.getString("child_id", null) + "/"+
//                                 Util.getLongHour0(), null,
//            new DataCallback() {
//                @Override
//                public void onDataReceiving(JSONObject data) throws Exception {
//                    Log.d("test", data.toString());
//                    JSONArray fetchedData = data.getJSONArray("data");
//                    List<LocationDTO> locList = new ArrayList<>();
//                    for (int i = 0; i < fetchedData.length(); i++){
//                        JSONObject loc = fetchedData.getJSONObject(i);
//                        LocationDTO newLoc = new LocationDTO(
//                            loc.getLong("safezoneId"),
//                            loc.getString("name"),
//                            loc.getDouble("latitude"),
//                            loc.getDouble("longitude"),
//                            loc.getInt("radius"),
//                            Util.timeStrToCalendar(loc.getString("fromTime")),
//                            Util.timeStrToCalendar(loc.getString("toTime")),
//                            loc.getString("type")
//                        );
//                        locList.add(newLoc);
//                    }
//                    Location.locList = locList;
//                }
//            }
//        ).execute();
    }

    // ------------------- CONFIG DISPLAY -------------------

    public void logToken(View view) {
        fcmService.getDeviceToken();
        Log.d("test", "Device token: " + FCMService.token);
        fetchConfig();
        // put device token to server
        JSONObject tokenJsonObj = new JSONObject();
        try {
            tokenJsonObj.put("childId", ref.getString("child_id", null))
                        .put("pushToken", FCMService.token);
        } catch (JSONException e){
            e.printStackTrace();
        }
        new HttpRequestSender("PUT", ref.getString("ip_port", null)+"/child/verify/parent", tokenJsonObj.toString(), null).execute();
    }

    private void fetchConfig(){
        String config =
            "Application configs: " +
            "\n- Child Id: " + ref.getString("child_id", null) +
            "\n- IP & Port: " + ref.getString("ip_port", null) +
            "\n- Interval: " + ref.getInt("report_interval", 0) +
            "\n- Token: " + fcmService.token;
        txtConfigInfo.setText(config);
    }

    // ------------------- CALLBACK FUNCTIONS -------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            String scannedCode = result.getContents();
            if (scannedCode != null) {
                SharedPreferences.Editor refEditor = ref.edit();
                refEditor.putString("ip_port", "http://" + scannedCode + ":8080");
                refEditor.apply();
                Toast.makeText(this, "Scanned IP: " + scannedCode, Toast.LENGTH_LONG).show();
                Log.d("test", "Scanned IP: " + scannedCode);
                fetchConfig();
            }
        }
    }

}
