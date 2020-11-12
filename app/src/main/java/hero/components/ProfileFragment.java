package hero.components;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

import hero.api.DataCallback;
import hero.data.HttpDAO;
import hero.data.QuestDAO;
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
        setData();

        setHiddenLoadData();

        return view;

    }

    private void setAvatar(){

//        Bitmap input = BitmapFactory.decodeResource(getResources(), R.drawable.kid_avatar);
//        Bitmap output = Bitmap.createBitmap(input.getWidth(), input.getHeight(), Bitmap.Config.ARGB_8888);
        byte[] decodedString = Base64.decode(spSupport.get("child_photo"), Base64.DEFAULT);
        Bitmap input = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Bitmap output = Bitmap.createBitmap(input.getWidth(), input.getHeight(), Bitmap.Config.ARGB_8888);
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

    private void setData(){

        txtName.setText(spSupport.get("child_name"));
        txtNickname.setText('(' + spSupport.get("child_nickname") + ')');

        List<Integer> badgeList = QuestDAO.getInstance(getActivity()).getListBadge();
        if (badgeList.size() > 0) {
            grvBadge.setAdapter(new BadgeAdapter(getActivity(), 0, badgeList));
        }

    }

    // ================== HIDDEN: LOAD ALL DATA ====================

    private void setHiddenLoadData(){
        imvAvatar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                loadAllData();
                return true;
            }
        });
    }

    private void loadAllData(){

        Context context = getActivity();
        String ipPort = spSupport.get("ip_port");
        String childId = spSupport.get("child_id");
        HttpDAO httpDao = HttpDAO.getInstance(context, ipPort);

        // load task data
        httpDao.getTaskList(childId);
        httpDao.getBadgeList(childId);

        // load quest data
        httpDao.getQuestList(childId);

        // load profile data
        httpDao.getChildInfo(childId, new DataCallback() {
            @Override
            public void onDataReceiving(JSONObject data) throws Exception {
                JSONObject childObj = data.getJSONObject("data");
                spSupport.set("child_name", childObj.getString("firstName") + " " + childObj.getString("lastName"));
                spSupport.set("child_nickname", childObj.getString("nickName"));
                spSupport.set("child_photo", childObj.getString("photo"));
                // show to UI
                txtName.setText(spSupport.get("child_name"));
                txtNickname.setText('(' + spSupport.get("child_nickname") + ')');
                setAvatar();
            }
        });

        Toast.makeText(context, "Load thành công!", Toast.LENGTH_LONG).show();

    }

}
