package hero.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import hero.main.R;

public class InfoDialog extends Dialog implements android.view.View.OnClickListener {

    Activity activity;
    LinearLayout dialogContainer;
    TextView btnOk;

    public InfoDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.info_dialog);

        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialogContainer = findViewById(R.id.dialog_info);
        btnOk = findViewById(R.id.btn_ok);

        Resources resource = activity.getResources();
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(resource.getColor(R.color.colorWhite));
        gd.setCornerRadius(10);
        dialogContainer.setBackground(gd);

        btnOk.setOnClickListener(this);
        gd = new GradientDrawable();
        gd.setColor(resource.getColor(R.color.colorYellow));
        gd.setCornerRadius(20);
        btnOk.setBackground(gd);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_ok){
            dismiss();
        }
    }

}