package hero.components;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import hero.api.DataCallback;
import hero.api.GETRequestSender;
import hero.main.R;

public class ProfileFragment extends Fragment {

    View view;
    HorizontalScrollView sliderContainer;
    ImageView imvAvatar;
    View nameContainer, taskSummaryContainer, questSummaryContainer;
    TextView txtName, txtNickname;
    View btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        sliderContainer = view.findViewById(R.id.sliderContainer);
        imvAvatar = view.findViewById(R.id.imvAvatar);
        nameContainer = view.findViewById(R.id.nameContainer);
        taskSummaryContainer = view.findViewById(R.id.taskSummaryContainer);
        questSummaryContainer = view.findViewById(R.id.questSummaryContainer);
        txtName = view.findViewById(R.id.txtName);
        txtNickname = view.findViewById(R.id.txtNickname);
        btnLogout = view.findViewById(R.id.btnLogout);

        setAvatar();
        setGraphic();
        setData();

        return view;

    }

    private void setAvatar(){

        Bitmap input = BitmapFactory.decodeResource(getResources(), R.drawable.kid_avatar);
        Bitmap output = Bitmap.createBitmap(input.getWidth(), input.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, input.getWidth(), input.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 1000;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(input, rect, rect, paint);
        imvAvatar.setImageBitmap(output);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setGraphic(){

        // name
        PaintDrawable pd = new PaintDrawable(getResources().getColor(R.color.colorTaskBackground));
        pd.setCornerRadius(20);
        nameContainer.setBackground(pd);
        // task summary
        pd = new PaintDrawable(getResources().getColor(R.color.colorTaskBackground));
        pd.setCornerRadius(20);
        taskSummaryContainer.setBackground(pd);
        // quest summary
        pd = new PaintDrawable(getResources().getColor(R.color.colorTaskBackground));
        pd.setCornerRadius(20);
        questSummaryContainer.setBackground(pd);
        // button logout
        pd = new PaintDrawable(Color.WHITE);
        pd.setCornerRadius(20);
        btnLogout.setBackground(pd);

        // slider to "logout" button
        sliderContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    final int xDes;
                    if (sliderContainer.getScrollX() >= 120) xDes = 240;
                    else xDes = 0;
                    sliderContainer.post(new Runnable() {
                        @Override
                        public void run() {
                            sliderContainer.smoothScrollTo(xDes, 0);
                        }
                    });
                }
                return false;
            }
        });

    }

    private void setData(){

        SharedPreferences ref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        new GETRequestSender(ref.getString("ip_port", null)+"/child/"+ref.getString("child_id", null),
                new DataCallback() {
                    @Override
                    public void onDataReceiving(JSONObject data) throws Exception {
                        Log.d("test", "Request response: " + data.toString());
                        JSONObject childObj = data.getJSONObject("data");
                        txtName.setText(childObj.getString("firstName") + " " + childObj.getString("lastName"));
                        txtNickname.setText('(' + childObj.getString("nickName") + ')');
                    }
                }
        ).execute();

    }

}
