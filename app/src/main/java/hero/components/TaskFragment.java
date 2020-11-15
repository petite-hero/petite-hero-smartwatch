package hero.components;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import hero.data.TaskDAO;
import hero.data.TaskDTO;
import hero.main.R;

public class TaskFragment extends Fragment {

    View view;
    ListView livTaskActive, livTaskLate;
    View taskListSeparator;
    TextView txtListPlaceholder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_task, container, false);

        livTaskActive = view.findViewById(R.id.taskListActive);
        livTaskLate = view.findViewById(R.id.taskListLate);
        taskListSeparator = view.findViewById(R.id.taskListSeparator);
        txtListPlaceholder = view.findViewById(R.id.txtListPlaceholder);

        applyTaskList();

        return view;

    }

    private void applyTaskList(){

        TaskDAO taskDao = TaskDAO.getInstance(getActivity());
        List<TaskDTO> taskListActive = taskDao.getActiveTask();
        List<TaskDTO> taskListLate = taskDao.getListLate();

        if (taskListActive.size() > 0) livTaskActive.setAdapter(new TaskAdapter(getActivity(), 0, taskListActive, true));
        else livTaskActive.setVisibility(View.GONE);

        if (taskListLate.size() > 0){
            livTaskLate.setAdapter(new TaskAdapter(getActivity(), 0, taskListLate, false));
            livTaskLate.getLayoutParams().height = 170*taskListLate.size() - 10;
        } else livTaskLate.setVisibility(View.GONE);

        if (taskListActive.size() > 0 && taskListLate.size() > 0) taskListSeparator.setVisibility(View.VISIBLE);
        if (taskListActive.size() == 0 && taskListLate.size() == 0) txtListPlaceholder.setVisibility(View.VISIBLE);

    }

}
