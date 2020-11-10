package hero.components;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import hero.data.QuestDTO;
import hero.main.R;

public class QuestFragment extends Fragment {

    View view;
    HorizontalScrollView sliderContainer;
    ListView livQuest;
    GridView grvBadge;

    List<QuestDTO> questList;
    List<String> badgeList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_quest, container, false);

        sliderContainer = view.findViewById(R.id.sliderContainer);
        livQuest = view.findViewById(R.id.questList);
        grvBadge = view.findViewById(R.id.badgeList);

        setGraphic();
        fetchQuestList();
        fetchBadgeList();

        return view;

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setGraphic() {
        // slider to badges screen
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

    private void fetchQuestList(){

        questList = new ArrayList<>();

        questList.add(new QuestDTO("Biết chạy xe đạp", "badge_01", "Chạy được xe đạp trong hè này", "#ff0000"));
        questList.add(new QuestDTO("Điểm top 10 lớp", "badge_24", "Điểm trung bình top 10 lớp", "#00ff00"));

//        SharedPreferences ref = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        new GETRequestSender(ref.getString("ip_port", null)+"/quest/list/"+ref.getString("child_id", null)+"?status=ASSIGNED",
//                new DataCallback() {
//                    @Override
//                    public void onDataReceiving(JSONObject data) throws Exception {
//                        Log.d("test", "Request response: " + data.toString());
//                        JSONObject childObj = data.getJSONObject("data");
//                        txtName.setText(childObj.getString("firstName") + " " + childObj.getString("lastName"));
//                        txtNickname.setText('(' + childObj.getString("nickName") + ')');
//                    }
//                }
//        ).execute();

        livQuest.setAdapter(new QuestAdapter(getActivity(), 0, questList));
        livQuest.getLayoutParams().height = 170*questList.size() - 10;

    }

    private void fetchBadgeList(){
        badgeList = new ArrayList<>();
        badgeList.add("badge_01");
        badgeList.add("badge_24");
        badgeList.add("badge_27");
        badgeList.add("badge_01");
        badgeList.add("badge_24");
//        badgeList.add("badge_27");
//        badgeList.add("badge_01");
//        badgeList.add("badge_24");
//        badgeList.add("badge_27");
        grvBadge.setAdapter(new BadgeAdapter(getActivity(), 0, badgeList));
    }

}
