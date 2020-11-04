package hero.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();  // hide system status bar
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

}
