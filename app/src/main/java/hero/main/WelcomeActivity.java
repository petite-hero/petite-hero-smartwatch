package hero.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import hero.api.DataCallback;
import hero.data.HttpDAO;
import hero.service.FCMService;
import hero.util.SPSupport;

public class WelcomeActivity extends Activity{

    private static final boolean IS_SKIP_LOGIN = true;  // testing

    Button btnScanQR;
    SPSupport spSupport;
    FCMService fcmService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btnScanQR = findViewById(R.id.btnScanQR);
        spSupport = new SPSupport(this);
        fcmService = new FCMService();
        fcmService.getDeviceToken();

        initLayout();

    }

    private void initLayout(){

        // scan button
        PaintDrawable pd = new PaintDrawable(getResources().getColor(R.color.colorStrongCyan));
        pd.setCornerRadius(20);
        btnScanQR.setBackground(pd);

        // listener to open hidden developer mode
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
        // hide system status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // EVENT HANDLER: SCAN BUTTON PRESSED
    public void scanQRCode(View view) {

        // TEST - skip login
        if (IS_SKIP_LOGIN) {
            String childId = "3";
            // set child id
            spSupport.set("child_id", childId);
            // open MainScreenActivity
            Intent intent = new Intent(WelcomeActivity.this, MainScreenActivity.class);
            intent.putExtra("fragment", 2);
            finish();
            startActivity(intent);
            // put device token to server
            HttpDAO.getInstance(this, spSupport.get("ip_port")).putDeviceToken(childId, FCMService.token, null);
        }

        // scan child id
        else {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt("Scan");
            integrator.setCameraId(0);  // Use a specific camera of the device
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {

            // SAVE SCANNED CHILD ID & REPORT DEVICE TOKEN TO SERVER

            // get ID
            String scannedCode = result.getContents();
            // save child ID to local
            spSupport.set("child_id", scannedCode);

            // save device token to server
            if (FCMService.token == null || FCMService.token.length() == 0){
                Toast.makeText(this, "Có lỗi xảy ra. Vui lòng thử lại.", Toast.LENGTH_LONG).show();
                return;
            }
            HttpDAO.getInstance(this, spSupport.get("ip_port")).putDeviceToken(scannedCode, FCMService.token, new DataCallback() {
                @Override
                public void onDataReceiving(JSONObject data) throws Exception {
                    if (data == null){
                        Toast.makeText(WelcomeActivity.this, "Có lỗi xảy ra. Vui lòng thử lại.", Toast.LENGTH_LONG).show();
                        return;
                    } else{
                        // MOVE TO MAIN ACTIVITY
                        Intent intent = new Intent(WelcomeActivity.this, MainScreenActivity.class);
                        intent.putExtra("fragment", 2);
                        finish();
                        startActivity(intent);
                        Toast.makeText(WelcomeActivity.this, "Kết nối thành công!", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

}