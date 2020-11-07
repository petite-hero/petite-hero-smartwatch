package hero.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import hero.api.DataCallback;
import hero.api.PUTRequestSender;
import hero.service.FCMService;

public class WelcomeActivity extends Activity{

    Button btnScanQR;
    SharedPreferences ref;
    FCMService fcmService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btnScanQR = findViewById(R.id.btnScanQR);
        ref = PreferenceManager.getDefaultSharedPreferences(this);
        fcmService = new FCMService();
        fcmService.getDeviceToken();

        btnScanQR.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, DeveloperActivity.class);
                startActivity(intent);
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();  // hide system status bar
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public void scanQRCode(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {

            // get ID
            String scannedCode = result.getContents();
            if (scannedCode != null) {
                SharedPreferences.Editor refEditor = ref.edit();
                refEditor.putString("child_id", scannedCode);
                refEditor.apply();
                new AlertDialog.Builder(this)
                    .setMessage("Child ID set as " + ref.getString("child_id", null))
                    .setPositiveButton("OK", null)
                    .show();
                Toast.makeText(this, "Scanned ID: " + scannedCode, Toast.LENGTH_LONG).show();
                Log.d("test", "Scanned ID: " + scannedCode);
            }

            // put device token to server
            JSONObject tokenJsonObj = new JSONObject();
            try {
                tokenJsonObj.put("childId", scannedCode)
                        .put("pushToken", FCMService.token);
                Log.d("test", "device token: " + FCMService.token);
            } catch (JSONException e){
                e.printStackTrace();
            }
            new PUTRequestSender(ref.getString("ip_port", null)+"/child/verify/parent", tokenJsonObj.toString(),
                new DataCallback() {
                    @Override
                    public void onDataReceiving(JSONObject data) throws Exception {
                        Log.d("test", data.toString());
                    }
                }
            ).execute();
            Log.d("test", ref.getString("ip_port", null)+"/child/verify/parent");
            Log.d("test", tokenJsonObj.toString());

        }
    }

}