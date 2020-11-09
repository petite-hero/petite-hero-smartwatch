package hero.components;

import android.annotation.SuppressLint;
import android.app.Fragment;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import hero.main.R;

public class ProfileFragment extends Fragment {

    View view;
    HorizontalScrollView sliderContainer;
    ImageView imvAvatar;
    View nameContainer, taskSummaryContainer, questSummaryContainer;
    View btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        sliderContainer = view.findViewById(R.id.sliderContainer);
        imvAvatar = view.findViewById(R.id.imvAvatar);
        nameContainer = view.findViewById(R.id.nameContainer);
        taskSummaryContainer = view.findViewById(R.id.taskSummaryContainer);
        questSummaryContainer = view.findViewById(R.id.questSummaryContainer);
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
        PaintDrawable pd = new PaintDrawable(getResources().getColor(R.color.colorStrongCyan));
        pd.setCornerRadius(20);
        nameContainer.setBackground(pd);
        // task summary
        pd = new PaintDrawable(Color.WHITE);
        pd.setCornerRadius(20);
        taskSummaryContainer.setBackground(pd);
        // quest summary
        pd = new PaintDrawable(Color.WHITE);
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

    }

}
