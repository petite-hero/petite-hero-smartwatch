package hero.components;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hero.api.DataCallback;
import hero.api.GETRequestSender;
import hero.data.TaskDAO;
import hero.data.TaskDTO;
import hero.main.R;
import hero.util.Util;

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

        fetchTaskList();

        return view;

    }

    public void fetchTaskList(){
        SharedPreferences ref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        new GETRequestSender(ref.getString("ip_port", null)+"/task/list/"+ref.getString("child_id", null)+"?date="+ Util.getLongHour0()+"&provider=sw",
                new DataCallback() {
                    @Override
                    public void onDataReceiving(JSONObject data) throws Exception {
                        Log.d("test", "Request response: " + data.toString());
                        JSONArray rowJsonArr = data.getJSONArray("data");
                        List<TaskDTO> taskList = new ArrayList<>();
                        for (int i = 0; i < rowJsonArr.length(); i++) {
                            JSONObject rowJsonObj = rowJsonArr.getJSONObject(i);
                            long id = rowJsonObj.getLong("taskId");
                            String name = rowJsonObj.getString("name");
                            String status = rowJsonObj.getString("status");
                            Calendar fromTime = Util.timeStrToCalendar(rowJsonObj.getString("fromTime"));
                            Calendar toTime = Util.timeStrToCalendar(rowJsonObj.getString("toTime"));
                            String type = rowJsonObj.getString("type");
                            String description = rowJsonObj.getString("description");
                            taskList.add(new TaskDTO(id, name, type, description, fromTime, toTime, status));
                        }
                        TaskDAO.getInstance(getActivity()).saveList(taskList);
                        applyTaskList();
                    }
                }
        ).execute();
    }

    private void applyTaskList(){

        List<TaskDTO> taskList = TaskDAO.getInstance(getActivity()).getActiveTask();
        List<TaskDTO> taskList2 = TaskDAO.getInstance(getActivity()).getListLate();
        if (taskList.size() > 0) livTaskActive.setAdapter(new TaskAdapter(getActivity(), 0, taskList, true));
        if (taskList2.size() > 0) livTaskLate.setAdapter(new TaskAdapter(getActivity(), 0, taskList2, false));
        livTaskLate.getLayoutParams().height = 170*taskList2.size() - 10;


//        taskList = new ArrayList<>();
//        taskList.add(new TaskDTO(0, "Lau nhà", "Housework", "Lau phòng khách đến nhà bếp", Calendar.getInstance(), Calendar.getInstance(), "ASSIGNED"));
//        livTaskActive.setAdapter(new TaskAdapter(getActivity(), 0, taskList, true));
//
//        List<TaskDTO> taskList2 = new ArrayList<>();
//        taskList2.add(new TaskDTO(0, "Lau nhà hiều vô", "Housework", "Lau phòng khách đến nhà bếp", Calendar.getInstance(), Calendar.getInstance(), "ASSIGNED"));
//        taskList2.add(new TaskDTO(2, "Lau nhà nhiề", "Housework", "Lau phòng khách đến nhà bếp", Calendar.getInstance(), Calendar.getInstance(), "ASSIGNED"));
//        livTaskLate.setAdapter(new TaskAdapter(getActivity(), 0, taskList2, false));
//        livTaskLate.getLayoutParams().height = 170*taskList2.size() - 10;

        if (taskList.size() > 0 && taskList2.size() > 0) taskListSeparator.setVisibility(View.VISIBLE);

    }

}
