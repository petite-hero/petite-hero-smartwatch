package hero.components;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import hero.data.TaskDAO;
import hero.data.TaskDTO;
import hero.main.R;

public class TaskFragment extends Fragment {

    View view;
    ListView livTaskActive, livTaskLate;
    View taskListSeparator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_task, container, false);

        livTaskActive = view.findViewById(R.id.taskListActive);
        livTaskLate = view.findViewById(R.id.taskListLate);
        taskListSeparator = view.findViewById(R.id.taskListSeparator);

        applyTaskList();

        return view;

    }

    private void applyTaskList(){

        List<TaskDTO> taskListActive = TaskDAO.getInstance(getActivity()).getActiveTask();
        List<TaskDTO> taskListLate = TaskDAO.getInstance(getActivity()).getListLate();

        if (taskListActive.size() > 0) livTaskActive.setAdapter(new TaskAdapter(getActivity(), 0, taskListActive, true));
        if (taskListLate.size() > 0) livTaskLate.setAdapter(new TaskAdapter(getActivity(), 0, taskListLate, false));
        livTaskLate.getLayoutParams().height = 170*taskListLate.size() - 10;

        if (taskListActive.size() == 0) livTaskActive.setVisibility(View.GONE);
        else  livTaskActive.setVisibility(View.VISIBLE);
        if (taskListActive.size() > 0 && taskListLate.size() > 0) taskListSeparator.setVisibility(View.VISIBLE);
        else  taskListSeparator.setVisibility(View.GONE);

    }

}
