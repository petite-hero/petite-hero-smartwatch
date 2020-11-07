package hero.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import hero.api.Multipart;
import hero.components.ProfileFragment;
import hero.components.QuestFragment;
import hero.components.TaskFragment;

public class MainScreenActivity extends Activity {

    private static final int[] ICON_NAV_LIST = {R.drawable.task, R.drawable.quest, R.drawable.profile};
    private static final int[] ICON_NAV_ACTIVE_LIST = {R.drawable.task_active, R.drawable.quest_active, R.drawable.profile_active};
    private static final int CAPTURE_REQUEST = 123;

    ImageButton[] btnNavList;
    Fragment[] fragmentList;
    FragmentManager fragmentManager;

    int currentFragmentIndex;
    Uri outputUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        btnNavList = new ImageButton[3];
        btnNavList[0] = findViewById(R.id.btnNavTask);
        btnNavList[1] = findViewById(R.id.btnNavQuest);
        btnNavList[2] = findViewById(R.id.btnNavProfile);

        fragmentManager = getFragmentManager();
        fragmentList = new Fragment[3];

        currentFragmentIndex = 0;
        fragmentList[0] = new TaskFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragmentList[0]);
        fragmentTransaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();  // hide system status bar
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public void loadFragmentFromBtnPress(View view){

        int btnIndex = Integer.parseInt(view.getTag().toString());
        if (fragmentList[btnIndex] == null){
            if (btnIndex == 1) fragmentList[btnIndex] = new QuestFragment();
            else if (btnIndex == 2) fragmentList[btnIndex] = new ProfileFragment();
        }

        btnNavList[currentFragmentIndex].setImageResource(ICON_NAV_LIST[currentFragmentIndex]);
        btnNavList[btnIndex].setImageResource(ICON_NAV_ACTIVE_LIST[btnIndex]);
        currentFragmentIndex = btnIndex;

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragmentList[btnIndex]);
        fragmentTransaction.commit();

    }

    public void flipTask(View view){

        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f);
        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.setDuration(200);
        oa2.setDuration(200);

        final View taskContainer =  view;
        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                super.onAnimationEnd(animation);

                View taskFrontFaceContainer = taskContainer.findViewById(R.id.taskFrontFaceContainer);
                View taskBackFaceContainer = taskContainer.findViewById(R.id.taskBackFaceContainer);
                // front to back
                if (taskFrontFaceContainer.getVisibility() == View.VISIBLE) {
                    taskFrontFaceContainer.setVisibility(View.GONE);
                    taskBackFaceContainer.setVisibility(View.VISIBLE);
                // back to front
                } else{
                    taskFrontFaceContainer.setVisibility(View.VISIBLE);
                    taskBackFaceContainer.setVisibility(View.GONE);
                }

                oa2.start();

            }
        });

        oa1.start();

    }

    public void takeImage(View view){

        String directoryName = Environment.getExternalStorageDirectory() + "/PetiteHero";
        File directory = new File(directoryName);
        if (! directory.exists()) directory.mkdir();
        String fileName = "task-" + Calendar.getInstance().getTimeInMillis() + ".jpg";
        File outputFile = new File(directoryName + "/" + fileName);
        outputUri = Uri.fromFile(outputFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        startActivityForResult(intent, CAPTURE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAPTURE_REQUEST) {
                final Map<String, String> params = new HashMap<>(2);
                SharedPreferences ref = PreferenceManager.getDefaultSharedPreferences(this);
                final String url = ref.getString("ip_port", null)+"/task/36/submit";
                Thread thread = new Thread(){
                    public void run(){
                        try {
                            String result = Multipart.multipartRequest(url, params, outputUri.getPath(), "proof", "image/jpeg");
                            Log.d("test", "Image Updated");
                        } catch (Exception e){
                            Log.d("test", e.toString());
                        }
                    }
                };
                thread.start();
            }
        }
    }
}
