package hero.components;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import hero.data.TaskDAO;
import hero.data.TaskDTO;
import hero.main.R;
import hero.util.TaskAdapter;

public class TaskFragment extends Fragment {

    View view;
    ListView livTaskProgress, livTaskFinish;

    List<TaskDTO> taskList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_task, container, false);

        livTaskProgress = view.findViewById(R.id.task_progress_list);
        livTaskFinish = view.findViewById(R.id.task_finish_list);

        fetchTaskList();
        applyTaskList();

        return view;

    }

    private void fetchTaskList(){
        taskList = TaskDAO.getInstance(getActivity()).getList();
//        taskList = new ArrayList<>();
//        taskList.add(new TaskDTO("Clean the house", "Housework", "Detail", Calendar.getInstance(), Calendar.getInstance(), "DONE"));
//        taskList.add(new TaskDTO("Name", "Education", "Detail", Calendar.getInstance(), Calendar.getInstance(), "HANDED"));
//        taskList.add(new TaskDTO("Name", "Skills", "Detail", Calendar.getInstance(), Calendar.getInstance(), "FAILED"));
//        taskList.add(new TaskDTO("Name", "Housework", "Detail", Calendar.getInstance(), Calendar.getInstance(), "ASSIGNED"));
//        taskList.add(new TaskDTO("Name", "Education", "Detail", Calendar.getInstance(), Calendar.getInstance(), "FAILED"));
//        taskList.add(new TaskDTO("Name", "Skills", "Detail", Calendar.getInstance(), Calendar.getInstance(), "ASSIGNED"));
    }

    private void applyTaskList(){

        // split into 2 columns
        List<TaskDTO> taskListProgress = new ArrayList<>();
        for (TaskDTO task : taskList) if (task.getStatus().equals("ASSIGNED")) taskListProgress.add(task);

        List<TaskDTO> taskListFinish = new ArrayList<>();
        for (TaskDTO task : taskList) if (task.getStatus().equals("HANDED")) taskListFinish.add(task);
        for (TaskDTO task : taskList) if (task.getStatus().equals("DONE")) taskListFinish.add(task);
        for (TaskDTO task : taskList) if (task.getStatus().equals("FAILED")) taskListFinish.add(task);

        // apply to 2 columns
        livTaskProgress.setAdapter(new TaskAdapter(getActivity(), 0, taskListProgress));
        livTaskFinish.setAdapter(new TaskAdapter(getActivity(), 0, taskListFinish));

    }

}
