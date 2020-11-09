package hero.components;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import hero.data.QuestDTO;
import hero.main.R;

public class QuestFragment extends Fragment {

    View view;
    ListView livQuest;
    GridView grvBadge;

    List<QuestDTO> questList;
    List<String> badgeList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_quest, container, false);

        livQuest = view.findViewById(R.id.questList);
        grvBadge = view.findViewById(R.id.badgeList);
        grvBadge.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 50));

        fetchQuestList();
        fetchBadgeList();

        return view;

    }

    private void fetchQuestList(){
        questList = new ArrayList<>();
        questList.add(new QuestDTO("Biết chạy xe đạp", "badge_01", "Chạy được xe đạp trong hè này", "#ff0000"));
        questList.add(new QuestDTO("Điểm top 10 lớp", "badge_24", "Điểm trung bình top 10 lớp", "#00ff00"));
        livQuest.setAdapter(new QuestAdapter(getActivity(), 0, questList));
    }

    private void fetchBadgeList(){
        badgeList = new ArrayList<>();
        badgeList.add("badge_01");
        badgeList.add("badge_24");
        badgeList.add("badge_27");
        badgeList.add("badge_01");
        badgeList.add("badge_24");
        badgeList.add("badge_27");
        grvBadge.setAdapter(new BadgeAdapter(getActivity(), 0, badgeList));
    }

}
