package hero.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.PaintDrawable;
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

import hero.api.MultipartHandler;
import hero.components.ProfileFragment;
import hero.components.QuestFragment;
import hero.components.TaskFragment;
import hero.service.LocationService;

public class MainScreenActivity extends Activity {

    // constances
    private static final int[] ICON_NAV_LIST = {R.drawable.task, R.drawable.quest, R.drawable.profile};
    private static final int[] ICON_NAV_ACTIVE_LIST = {R.drawable.task_active, R.drawable.quest_active, R.drawable.profile_active};
    private static final int CAPTURE_REQUEST = 123;

    // components
    ImageButton[] btnNavList;
    View navBarFocusHighlightContainer;
    Fragment[] fragmentList;
    FragmentManager fragmentManager;

    // supports
    int currentFragmentIndex;
    Uri outputUri;
    long currentTaskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // initialize nav bar
        btnNavList = new ImageButton[3];
        btnNavList[0] = findViewById(R.id.btnNavTask);
        btnNavList[1] = findViewById(R.id.btnNavQuest);
        btnNavList[2] = findViewById(R.id.btnNavProfile);

        navBarFocusHighlightContainer = findViewById(R.id.navBarFocusHighlightContainer);
        View navBarFocusHighlight = findViewById(R.id.navBarFocusHighlight);
        PaintDrawable pd = new PaintDrawable(getResources().getColor(R.color.colorGray));
        pd.setCornerRadius(2);
        navBarFocusHighlight.setBackground(pd);

        // initialize fragments
        fragmentManager = getFragmentManager();
        fragmentList = new Fragment[3];

        if (getIntent().getBooleanExtra("isLogin", false)) loadFragment(2);
        else loadFragment(0);

        // hidden logout
        btnNavList[2].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                logout();
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


    // ==================== NAVIGATION BAR EVENT HANDLER ====================

    public void loadFragmentFromBtnPress(View view){
        int btnIndex = Integer.parseInt(view.getTag().toString());
        loadFragment(btnIndex);
    }

    private void loadFragment(int fragmentIndex){

        // change nav bar icon
        btnNavList[currentFragmentIndex].setImageResource(ICON_NAV_LIST[currentFragmentIndex]);
        btnNavList[fragmentIndex].setImageResource(ICON_NAV_ACTIVE_LIST[fragmentIndex]);
        currentFragmentIndex = fragmentIndex;

        // animate nav bar focus
        ValueAnimator animator = ValueAnimator.ofInt(navBarFocusHighlightContainer.getPaddingLeft(), 66+fragmentIndex*36);  // calculated
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator){
                navBarFocusHighlightContainer.setPadding((Integer) valueAnimator.getAnimatedValue(), 0, 0, 0);
            }
        });
        animator.setDuration(100);
        animator.start();

        // initialize fragment if not loaded
        if (fragmentList[fragmentIndex] == null){
            if (fragmentIndex == 0) fragmentList[fragmentIndex] = new TaskFragment();
            else if (fragmentIndex == 1) fragmentList[fragmentIndex] = new QuestFragment();
            else if (fragmentIndex == 2) fragmentList[fragmentIndex] = new ProfileFragment();
        }

        // replace fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragmentList[fragmentIndex]);
        fragmentTransaction.commit();

    }


    // ==================== TASK FRAGMENT EVENT HANDLER ====================

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

    public void takeProofImage(long taskId){

        currentTaskId = taskId;

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


    // ==================== TASK FRAGMENT EVENT HANDLER ====================

    public void flipQuest(View view){

        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f);
        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.setDuration(200);
        oa2.setDuration(200);

        final View questContainer = view;
        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                View questFrontFaceContainer = questContainer.findViewById(R.id.questFrontFaceContainer);
                View questBackFaceContainer = questContainer.findViewById(R.id.questBackFaceContainer);
                // front to back
                if (questFrontFaceContainer.getVisibility() == View.VISIBLE) {
                    questFrontFaceContainer.setVisibility(View.GONE);
                    questBackFaceContainer.setVisibility(View.VISIBLE);
                // back to front
                } else{
                    questFrontFaceContainer.setVisibility(View.VISIBLE);
                    questBackFaceContainer.setVisibility(View.GONE);
                }
                oa2.start();
            }
        });

        oa1.start();

    }


    // ==================== PROFILE FRAGMENT EVENT HANDLER ====================

    private void logout(){

        LocationService.isRunning = false;

        SharedPreferences ref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor refEditor = ref.edit();
        refEditor.putString("child_id", null);
        refEditor.apply();

        Intent intent = new Intent(this, WelcomeActivity.class);
        finish();
        startActivity(intent);

    }


    // ==================== ACTIVITY RESULT HANDLER ====================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            // HANDLE IMAGE RECEIVED WHEN SUBMIT TASK
            if (requestCode == CAPTURE_REQUEST) {
                final Map<String, String> params = new HashMap<>(2);
                SharedPreferences ref = PreferenceManager.getDefaultSharedPreferences(this);
                final String url = ref.getString("ip_port", null)+"/task/" + currentTaskId + "/submit";
                Thread thread = new Thread(){
                    public void run(){
                        try {
                            String result = MultipartHandler.multipartRequest(url, params, outputUri.getPath(), "proof", "image/jpeg");
                            Log.d("test", "Image Updated");
                        } catch (Exception e){
                            Log.d("test", e.toString());
                        }
                    }
                };
                thread.start();
                new AlertDialog.Builder(MainScreenActivity.this)
                        .setMessage("Ảnh đã được gửi đi")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                                FragmentTransaction ft = MainScreenActivity.this.getFragmentManager().beginTransaction();
                                ft.detach(fragmentList[0]);
                                ft.attach(fragmentList[0]);
                                ft.commit();
                            }
                        })
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                                FragmentTransaction ft = MainScreenActivity.this.getFragmentManager().beginTransaction();
                                ft.detach(fragmentList[0]);
                                ft.attach(fragmentList[0]);
                                ft.commit();
                            }
                        })
                        .show();
            }

        }
    }
}
