package hero.components;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import java.util.List;

import hero.data.QuestDAO;
import hero.data.QuestDTO;
import hero.main.R;

public class QuestFragment extends Fragment {

    View view;
    ListView livQuest;
    GridView grvBadge;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_quest, container, false);

        livQuest = view.findViewById(R.id.questList);
        grvBadge = view.findViewById(R.id.badgeList);

        setData();

        return view;

    }

    private void setData(){
        List<QuestDTO> questList = QuestDAO.getInstance(getActivity()).getList();
        if (questList.size() > 0) {
            livQuest.setAdapter(new QuestAdapter(getActivity(), 0, questList));
            livQuest.getLayoutParams().height = 170 * questList.size() - 10;
        }
    }

}
