package hero.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class EntranceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

//        Intent intent = new Intent(this, WelcomeActivity.class);
        Intent intent = new Intent(this, MainScreenActivity.class);
        startActivity(intent);

    }
}
