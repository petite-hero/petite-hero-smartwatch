package hero.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import hero.api.DataCallback;
import hero.data.HttpDAO;
import hero.service.FCMService;
import hero.util.SPSupport;

public class WelcomeActivity extends Activity{

    ImageView imvLogo;
    TextView txtWelcome;
    Button btnScanQR;
    ProgressDialog progressDialog;
    SPSupport spSupport;
    FCMService fcmService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        imvLogo = findViewById(R.id.imvLogo);
        txtWelcome = findViewById(R.id.txtWelcome);
        btnScanQR = findViewById(R.id.btnScanQR);
        spSupport = new SPSupport(this);
        fcmService = new FCMService();
        fcmService.getDeviceToken();

        initLayout();

    }

    private void initLayout(){

        // welcome text
        txtWelcome.setTypeface(ResourcesCompat.getFont(this, R.font.montserrat_b));

        // scan button
        PaintDrawable pd = new PaintDrawable(getResources().getColor(R.color.colorStrongCyan));
        pd.setCornerRadius(20);
        btnScanQR.setBackground(pd);
        btnScanQR.setTypeface(ResourcesCompat.getFont(this, R.font.acumin));

        // listener to open hidden developer mode
        btnScanQR.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, DeveloperActivity.class);
                startActivity(intent);
                return true;
            }
        });

        // listener to hidden auto login
        imvLogo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //        final String CHILD_ID = "80";
                final String CHILD_ID = "3";
                handleLogin(CHILD_ID);
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
        // scan child id
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
        if(result != null && result.getContents() != null) {  // handle QR scanned
            final String scannedCode = result.getContents();
            handleLogin(scannedCode);
        }
    }

    private void handleLogin(final String childId){

        // show progress dialog
        progressDialog = new ProgressDialog(this, R.style.AlertDialogStyle);
        progressDialog.setMessage("Vui lòng chờ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // SAVE SCANNED CHILD ID
        spSupport.set("child_id", childId);

        // GET DEVICE TOKEN, ANDROID ID, DEVICE NAME
        if (FCMService.token == null || FCMService.token.length() == 0){
            progressDialog.dismiss();
            Toast.makeText(this, "Có lỗi xảy ra. Vui lòng thử lại.", Toast.LENGTH_LONG).show();
            return;
        }
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceName = Build.MANUFACTURER  + " " + Build.MODEL;

        HttpDAO.getInstance(this, spSupport.get("ip_port")).putDeviceToken(childId, FCMService.token, androidId, deviceName, new DataCallback() {
            @Override
            public void onDataReceiving(JSONObject data) throws Exception {
                if (data == null){
                    Toast.makeText(WelcomeActivity.this, "Có lỗi xảy ra. Vui lòng thử lại.", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
                else{
                    // LOAD CHILD DATA
                    HttpDAO.getInstance(WelcomeActivity.this, spSupport.get("ip_port")).getAllData(childId, new DataCallback() {
                        @Override
                        public void onDataReceiving(JSONObject data) throws Exception {
                            // MOVE TO MAIN ACTIVITY
                            Intent intent = new Intent(WelcomeActivity.this, MainScreenActivity.class);
                            intent.putExtra("fragment", 2);
                            finish();
                            startActivity(intent);
                            Toast.makeText(WelcomeActivity.this, "Kết nối thành công!", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });

    }

}