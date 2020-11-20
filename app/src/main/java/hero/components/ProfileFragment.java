package hero.components;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import org.json.JSONObject;

import java.util.List;

import hero.api.DataCallback;
import hero.data.HttpDAO;
import hero.data.QuestDAO;
import hero.data.QuestDTO;
import hero.main.R;
import hero.util.SPSupport;

public class ProfileFragment extends Fragment {

    View view;
    ImageView imvAvatar;
    TextView txtName, txtNickname;
    GridView grvBadge;

    SPSupport spSupport;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        imvAvatar = view.findViewById(R.id.imvAvatar);
        txtName = view.findViewById(R.id.txtName);
        txtNickname = view.findViewById(R.id.txtNickname);
        grvBadge = view.findViewById(R.id.badgeList);

        spSupport = new SPSupport(getActivity());

        setAvatar();
        setLayout();
        setData();

        setHiddenLoadData();

        return view;

    }

    private void setAvatar(){

        Bitmap input;
        Bitmap output;
        if (spSupport.get("child_photo").equals("")){
            input = BitmapFactory.decodeResource(getResources(), R.drawable.kid_avatar);
            output = Bitmap.createBitmap(input.getWidth(), input.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            byte[] decodedString = Base64.decode(spSupport.get("child_photo"), Base64.DEFAULT);
            input = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            output = Bitmap.createBitmap(input.getWidth(), input.getHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, output.getWidth(), output.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 2000;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(input, rect, rect, paint);
        imvAvatar.setImageBitmap(output);

    }

    private void setLayout(){
        // set font for text views
        Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.acumin);
        txtName.setTypeface(typeface);
        txtNickname.setTypeface(typeface);
    }

    private void setData(){

        txtName.setText(spSupport.get("child_name"));
        String nickname = spSupport.get("child_nickname");
        if (!nickname.equals("")) txtNickname.setText('(' + spSupport.get("child_nickname") + ')');

        List<QuestDTO> badgeList = QuestDAO.getInstance(getActivity()).getList("done");
        if (badgeList.size() > 0) {
            grvBadge.setAdapter(new BadgeAdapter(getActivity(), 0, badgeList));
        }

    }

    // ================== HIDDEN: LOAD ALL DATA ====================

    private void setHiddenLoadData(){
        imvAvatar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
            HttpDAO.getInstance(ProfileFragment.this.getActivity(), spSupport.get("ip_port")).getAllData(spSupport.get("child_id"), new DataCallback() {
                @Override
                public void onDataReceiving(JSONObject data) throws Exception {
                    txtName.setText(spSupport.get("child_name"));
                    txtNickname.setText('(' + spSupport.get("child_nickname") + ')');
                    setAvatar();
                    Toast.makeText(getActivity(), "Load thành công", Toast.LENGTH_LONG).show();
                }
            });

            return true;
            }
        });
    }

}
