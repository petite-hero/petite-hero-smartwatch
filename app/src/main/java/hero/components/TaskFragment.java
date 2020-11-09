package hero.components;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hero.data.TaskDTO;
import hero.main.R;

public class TaskFragment extends Fragment {

    View view;
    ListView livTaskActive, livTaskLate;

    List<TaskDTO> taskList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_task, container, false);

        livTaskActive = view.findViewById(R.id.taskListActive);
        livTaskLate = view.findViewById(R.id.taskListLate);

        fetchTaskList();

        return view;

    }

    private void fetchTaskList(){

//        taskList = TaskDAO.getInstance(getActivity()).getList();
        taskList = new ArrayList<>();
        taskList.add(new TaskDTO(0, "Lau nhà", "Housework", "Lau phòng khách đến nhà bếp", Calendar.getInstance(), Calendar.getInstance(), "ASSIGNED"));
//        taskList.add(new TaskDTO(0, "Lau nhà nhiều vô", "Housework", "Lau phòng khách đến nhà bếp", Calendar.getInstance(), Calendar.getInstance(), "ASSIGNED"));
        livTaskActive.setAdapter(new TaskAdapter(getActivity(), 0, taskList, true));

        List<TaskDTO> taskList2 = new ArrayList<>();
        taskList2.add(new TaskDTO(0, "Lau nhà nhiều vô", "Housework", "Lau phòng khách đến nhà bếp", Calendar.getInstance(), Calendar.getInstance(), "ASSIGNED"));
        livTaskLate.setAdapter(new TaskAdapter(getActivity(), 0, taskList2, false));

    }

}
