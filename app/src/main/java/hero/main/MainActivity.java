package hero.main;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import hero.api.DataCallback;
import hero.service.LocationService;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends Activity implements DataCallback{

    TextView txtLocationStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLocationStatus = findViewById(R.id.txtLocationStatus);

    }

    @Override
    public void onDataReceiving(JSONObject data) throws Exception {

    }

    public void startLocationService(View view){
        if (!LocationService.isRunning) {
            Intent locationIntent = new Intent(this, LocationService.class);
            startService(locationIntent);
        }
        LocationService.isRunning = true;
        txtLocationStatus.setText("Location service started");
    }

    public void stopLocationService(View view){
        LocationService.isRunning = false;
        txtLocationStatus.setText("Location service stopping requested. Wait 20 secs");
    }

    public void clickToScanQRCode(View view) {
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
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}