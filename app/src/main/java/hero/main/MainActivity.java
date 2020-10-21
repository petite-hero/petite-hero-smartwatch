package hero.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends Activity{

    Button btnScanQR;
    TextView txtChildId;
    SharedPreferences ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnScanQR = findViewById(R.id.btnScanQR);
        txtChildId = findViewById(R.id.txtChildId);
        ref = PreferenceManager.getDefaultSharedPreferences(this);

        btnScanQR.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(MainActivity.this, DeveloperActivity.class);
                startActivity(intent);
                return true;
            }
        });

        txtChildId.setText("Child ID: " + ref.getString("child_id", null));

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
            String scannedCode = result.getContents();
            if (scannedCode == null) scannedCode = "Scanning cancelled";
            Toast.makeText(this, "Scanned ID: " + scannedCode, Toast.LENGTH_LONG).show();
            Log.d("test", "Scanned ID: " + scannedCode);
        }
    }

}